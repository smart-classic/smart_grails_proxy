package org.chip.mo

import java.util.Map;

import org.chip.mo.exceptions.MOCallException;
import org.chip.rdf.vitals.Encounter;
import org.chip.rdf.vitals.VitalSigns;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class EncountersCall extends MilleniumObjectCall{
	
	static final Map encounterResourceMap
	static final Map encounterTitleMap
	
	static{
		def config = ConfigurationHolder.config
		encounterResourceMap = config.cerner.mo.encounterResource
		encounterTitleMap = config.cerner.mo.encounterTitle
	}
	
	def init(){
		super.init()
		transaction = 'ReadEncountersByFilters'
		targetServlet = 'com.cerner.encounter.EncounterServlet'
	}
	
	/**
	* Generates MO requests to 
	* - Get Encounters for a given patient ID
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
	   Map encountersById
	   try{
		   encountersById = new HashMap()
		   def replyMessage = moResponse.getData()
		   def payload= replyMessage.Payload
		   //long l1 = new Date().getTime()
		   // Filter out inpatient encounters, per 12/19/2011 decision.
		   payload.Encounters.Encounter.findAll {
			 it.EncounterTypeClass.Display.text() != "Inpatient"
		   }.each {
			   Encounter encounter = new Encounter()
			   encounter.setStartDate(it.RegistrationDateTime.text())
			   encounter.setEndDate(it.DischargeDateTime.text())
			   encounter.getEncounterType().setCode(encounterResourceMap.get(it.EncounterTypeClass.Display.text()))
			   encounter.getEncounterType().setTitle(encounterTitleMap.get(it.EncounterTypeClass.Display.text()))
			   encountersById.put(it.EncounterId.text(), encounter)
		   }
		   //long l2 = new Date().getTime()
		   //println("encounter reading moresponse took: "+(l2-l1)/1000)
	   }catch(Exception e){
			throw new MOCallException("Error reading MO response", 500, e.getMessage())
	   }
	   return encountersById
   }
   
}
