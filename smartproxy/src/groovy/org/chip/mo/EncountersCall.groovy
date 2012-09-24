package org.chip.mo

import java.util.Map;

import org.chip.mo.exceptions.MOCallException;
import org.chip.rdf.vitals.Encounter;
import org.chip.rdf.vitals.VitalSigns;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

/**
* EncountersCall.groovy
* Purpose: Represents the Millennium Object call to filter and return Encounter information for a specific Person id.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class EncountersCall extends MilleniumObjectCall{
	
	public static final String ENCOUNTER_TYPE_CODING_SYSTEM='encounterType'
	
	static final Map encounterResourceMap
	static final Map encounterTitleMap
	static final Map codingSystemsMap
	static Map encounterClassMap
	
	static{
		def config = ConfigurationHolder.config
		encounterResourceMap = config.cerner.mo.encounterResource
		encounterTitleMap = config.cerner.mo.encounterTitle
		codingSystemsMap = config.cerner.mo.codingSystemMap
		
		encounterClassMap = new HashMap()
		encounterClassMap.put('393','Outpatient')
		encounterClassMap.put('391','Inpatient')
		encounterClassMap.put('389','Emergency')
	}
	
	def init(){
		super.init()
		transaction = 'ReadEncountersByFilters'
		targetServlet = 'com.cerner.encounter.EncounterServlet'
	}
	
	/**
	* Generates MO requests to get Encounters for a given patient ID
	* @param recordId
	* @return
	*/
   def generatePayload(){
	   def recordId = (String)requestParams.get(RECORDIDPARAM)
		builder.PersonId(recordId)
		builder.BypassOrganizationSecurityIndicator('true')
   }
   
   /**
   * Reads in the MO response and creates a map of Encounter objects to Encounter ID
   * @param moResponse
   * @return
   */
   def readResponse(moResponse)throws MOCallException{
	   processPayload(moResponse.getData().Payload)
   }
   
   def processPayload(payload)throws MOCallException{
	   Map encountersById
	   try{
		   encountersById = new HashMap()
		   // Filter out inpatient encounters, per 12/19/2011 decision.
		   payload.Encounters.Encounter.findAll {
			 it.EncounterTypeClass.Meaning.text() != "INPATIENT"
		   }.each {
			   Encounter encounter = new Encounter()
			   
			   encounter.setStartDate(it.RegistrationDateTime.text())
			   encounter.setEndDate(it.DischargeDateTime.text())
			   encounter.setPatientId(it.PersonId.text())
			   
			   def encounterTitle = encounterTitleMap.get(it.EncounterTypeClass.Display.text())
			   if(encounterTitle==null) encounterTitle = "Ambulatory encounter"
			   def encounterResource = encounterResourceMap.get(it.EncounterTypeClass.Display.text())
			   if(encounterResource==null) encounterResource = "ambulatory"
			   
			   encounter.getEncounterType().setTitle(encounterTitle)
			   
			   encounter.getEncounterType().getCode().setType("EncounterType")
			   encounter.getEncounterType().getCode().setTitle(encounterTitle)
			   encounter.getEncounterType().getCode().setSystem(codingSystemsMap.get(ENCOUNTER_TYPE_CODING_SYSTEM))
			   encounter.getEncounterType().getCode().setIdentifier(encounterResource)
			   
			   encountersById.put(it.EncounterId.text(), encounter)
		   }
	   }catch(Exception e){
			throw new MOCallException("Error reading MO response for Encounters", 500, e.getMessage())
	   }
	   return encountersById
   }
   
}
