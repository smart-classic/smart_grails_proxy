package org.chip.mo;

import org.apache.commons.logging.Log;
import org.chip.managers.VitalSignsManager;
import org.chip.mo.exceptions.MOCallException;
import org.chip.mo.exceptions.InvalidRequestException;
import org.chip.mo.model.Event;
import org.chip.rdf.Vitals;
import org.chip.rdf.vitals.*;
import org.chip.readers.EventsReader;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import groovy.xml.MarkupBuilder;
import groovy.xml.StreamingMarkupBuilder;
import groovyx.net.http.AsyncHTTPBuilder;
import groovyx.net.http.ContentType;

import org.apache.commons.logging.LogFactory;

import groovy.xml.StreamingMarkupBuilder

/**
* ResultsCall.groovy
* Purpose: Represents the Millennium Object call to filter and return Results information for a specific Person id.
* Only clinical information is returned.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class ResultsCall extends MilleniumObjectCall{
	private static final Log log = LogFactory.getLog(this)
	
	static Map eventSetNames
	
	static{
		def config = ConfigurationHolder.config
		eventSetNames = config.cerner.mo.eventSetNames
	}
	
	VitalSignsManager vitalSignsManager
	EventsReader eventsReader
	
	def init(){
		super.init()
		transaction = 'ReadResultsByCount'
		targetServlet = 'com.cerner.results.ResultsServlet'
		
		vitalSignsManager = new VitalSignsManager()
		eventsReader = new EventsReader()
	}
	
	/**
	* The Workhorse method. (overrides the base implementation)
	* -Creates the outgoing MO request.
	* -calls makeRestCall to make the actual MO call.
	* -calls readResponse to create an RDF Model from the MO response.
	* @param recordId
	* @param moURL
	* @return
	*/
   def makeCall(recordId, moURL) throws MOCallException{
	   	requestParams.put(RECORDIDPARAM, recordId)
	   	def resultsMap
	   	try{
		   def requestXML = createRequest()
	   
		   long l1 = new Date().getTime()
		   
		   resultsMap = makeAsyncCall(requestXML, moURL, recordId)
		   
		   long l2 = new Date().getTime()
		   log.info("Call for transaction: "+transaction+" took "+(l2-l1)/1000)
		   

		} catch (InvalidRequestException ire){
		   log.error(ire.exceptionMessage +" for "+ recordId +" because " + ire.rootCause)
		   throw new MOCallException(ire.exceptionMessage, ire.statusCode, ire.rootCause)
		}
		
		long l1 = new Date().getTime()
		def respXml = aggregateResults(resultsMap)
		long l2 = new Date().getTime()
		Vitals vitals = readResponse(respXml)
		long l3 = new Date().getTime()
		log.info("Aggregating MO response took "+(l2-l1)/1000)
		log.info("Reading and processing MO response took "+(l3-l2)/1000)
		return vitals
   }
   
   def makeAsyncCall(requestXML, moURL, recordId) throws MOCallException{
	   def async = new AsyncHTTPBuilder(
		   poolSize : 16,
		   uri : moURL+targetServlet,
		   contentType : ContentType.XML)
	
	   Map resultsMap = new HashMap()
	   eventSetNames.each {key, eventSetName->
		   String subRequestXml=requestXML.replace('EVENTSETNAME',eventSetName)
		   
		   def result = async.post(body:subRequestXml, requestContentType : ContentType.XML) { resp, xml ->
			   return xml
		   }
		   resultsMap.put(key, result)
	   }
	   
	   resultsMap.each{key, result->
		   while(!result.done){
			   log.info("ASYNC: Waiting for MO results")
			   Thread.sleep(500)
		   }
		   log.info(key+ " ASYNC MO call returned")
		   handleExceptions(result.get(), recordId)
	   }
	   
	   return resultsMap
   }
   
   def aggregateResults(resultsMap){
	   def clinicalEvents
	   def mapIdx=0
	   resultsMap.each{key, result->
		   //get the MO reply message from the http response
		   def replyMessage = result.get()
		   
		   //If it's the first element in the map, extract the ClinicalEvents xml element
		   if(mapIdx==0){
			   def payload = replyMessage.Payload
			   if(payload.size()==1){
				   clinicalEvents=replyMessage.Payload.Results.ClinicalEvents
				   mapIdx++
			   }
		   }else{//If we are past the first element, just append all Results elements to the ClinicalEvents xml element
			   def numericResults=replyMessage.Payload.Results.ClinicalEvents.NumericResult
			   def codedResults = replyMessage.Payload.Results.ClinicalEvents.CodedResult
			   
				for (numericResult in numericResults){
					clinicalEvents.appendNode(numericResult)
				}   
				
				for (codedResult in codedResults){
					clinicalEvents.appendNode(codedResult)
				}
		   }
	   }
	   
	   def outputBuilder = new StreamingMarkupBuilder()
	   outputBuilder.encoding="UTF-8"
	   def writer = new StringWriter()
	   writer<<outputBuilder.bind {mkp.yield clinicalEvents}
	   String aggregatedResponse = writer.toString()
	   def aggregatedResponseXml = new XmlSlurper().parseText(aggregatedResponse)
	   log.info("Printing aggregated response")
	   log.info(aggregatedResponse)
	   log.info("Done printing aggregated response")
	   return aggregatedResponseXml
   }
	
	/**
	* Generates MO requests to 
	* - Get Vitals for a list of Encounters and a given patient id
	* @param recordId
	* @return
	*/
	def generatePayload(){
		def recordId = (String)requestParams.get(RECORDIDPARAM)
		builder.PersonId(recordId)
		builder.EventCount('999')//We are only interested in the top 999 results for now.
		builder.EventSet(){
			Name('EVENTSETNAME')
		}
		
		Map encountersById = (HashMap)requestParams.get(MO_RESPONSE_PARAM)
		Set encounterIds = encountersById.keySet()
		
		// MO Server chokes on an empty encounter IDs list.
		// Log it and throw an exception.
		if (encounterIds.size() == 0) {
			throw new InvalidRequestException("Error creating MO Request", 500, "No Encounters Found")
		}

		builder.EncounterIds(){
			encounterIds.each{encounterId->
				EncounterId(encounterId)
			}
		}
	}
	
	/**
	* Pass the encountersById map containing all Encounters mapped to their id in.
	* This will be used by the manager to assign an encounter to each VitalSigns object it creates
	* Record results with the vitalSignsManager to process later.
	* 
	* @param moResponse
	* @return vitals
	*/
	def readResponse(moResponseXml)throws MOCallException{
		if(moResponseXml !=null){
			//parse the moResonse and pass events to the vitalSignsManager
			eventsReader.read(moResponseXml)
			Map eventsByParentEventId = eventsReader.getEvents()
			
			vitalSignsManager.recordEvents(eventsByParentEventId)
			vitalSignsManager.recordEncounters((HashMap)requestParams.get(MO_RESPONSE_PARAM, moResponseXml))
			vitalSignsManager.processEvents()
		}
			
		Vitals vitals = vitalSignsManager.getVitals()
		return vitals
	}
}