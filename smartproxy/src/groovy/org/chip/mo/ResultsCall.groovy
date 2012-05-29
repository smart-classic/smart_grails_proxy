package org.chip.mo;

import org.chip.managers.VitalSignsManager;
import org.chip.mo.exceptions.MOCallException;
import org.chip.mo.exceptions.InvalidRequestException;
import org.chip.mo.model.Event;
import org.chip.rdf.Vitals;
import org.chip.rdf.vitals.*;
import org.chip.readers.EventsReader;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import groovy.xml.MarkupBuilder;

class ResultsCall extends MilleniumObjectCall{
	
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
	   def resp
	   try{
		   def requestXML = createRequest()
	   
		   long l1 = new Date().getTime()
		   
		   resp = makeRestCall(requestXML, moURL)
		   
		   long l2 = new Date().getTime()
		   log.info("Call for transaction: "+transaction+" took "+(l2-l1)/1000)
		   
		   handleExceptions(resp, recordId)
	   } catch (InvalidRequestException ire){
		   log.error(ire.exceptionMessage +" for "+ recordId +" because " + ire.rootCause)
	   }
	   readResponse(resp)
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
		builder.EventCount('999')
		builder.EventSet(){
			Name('CLINICAL INFORMATION')
		}
		
		Map encountersById = (HashMap)requestParams.get(MO_RESPONSE_PARAM)
		Set encounterIds = encountersById.keySet()
		
		// MO Server chokes on an empty encounter IDs list.
		// Log it and throw an exception.
		if (encounterIds.size() == 0) {
			throw new InvalidRequestException("Error creating MO Request", 404, "No Encounters Found")
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
	* 
	* Read the MO response for Vitals:
	* 	Iterate through all NumericResults
	*		record each with the vitalSignsManager to process later.
	* 	Iterate through all CodedResults
	*		record each with the vitalSignsManager to process later.
	* @param moResponse
	* @return
	*/
	def readResponse(moResponse)throws MOCallException{
		if(moResponse !=null){
			//parse the moResonse and pass events to the vitalSignsManager
			eventsReader.read(moResponse)
			Map eventsByParentEventId = eventsReader.getEvents()
			
			vitalSignsManager.recordEvents(eventsByParentEventId)
			vitalSignsManager.recordEncounters((HashMap)requestParams.get(MO_RESPONSE_PARAM, moResponse))
			vitalSignsManager.processEvents()
		}
			
		Vitals vitals = vitalSignsManager.getVitals()
		return vitals
	}
}