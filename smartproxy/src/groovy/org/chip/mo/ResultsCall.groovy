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

/**
* ResultsCall.groovy
* Purpose: Represents the Millennium Object call to filter and return Results information for a specific Person id.
* Only clinical information is returned.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
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
			Name('CLINICAL INFORMATION')
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