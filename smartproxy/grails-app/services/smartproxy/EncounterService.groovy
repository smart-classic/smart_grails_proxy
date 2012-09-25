package smartproxy

import org.chip.mo.EncountersCall;
import org.chip.rdf.Demographics;
import org.chip.rdf.vitals.Code
import org.chip.rdf.vitals.CodedValue
import org.chip.rdf.vitals.Encounter;

class EncounterService {

	def sessionFactory
	
    static transactional = true
	
	private static final String RECORD_DELIM="RECORD_DELIM"
	private static final String PROP_DELIM="PROP_DELIM" 

    def getEncountersForPatient(patientId, unusedOnly) {
		if(unusedOnly){
			return Encounter.findAllByPatientIdAndUsed(patientId, false)
		}
		return Encounter.findAllByPatientId(patientId)
    }
	
	def getEncounterIdsForPatient(patientId){
		def encounters = getEncountersForPatient(patientId, true)
		Set encounterIds = new HashSet()
		encounters.each{encounter->
			encounterIds.add(encounter.encounterId)
		}
		return encounterIds
	}
	
	def getEncountersByIdForPatient(patientId){
		def encounters = getEncountersForPatient(patientId, false)
		Map encountersById = new HashMap()
		encounters.each{encounter ->
			encountersById.put(encounter.encounterId, encounter)
		}
		return encountersById
	}
	
	def markEncountersUsed(returnedEncounterIdsSet){
		returnedEncounterIdsSet.each{returnedEncounterId->
			Encounter returnedEncounter = Encounter.findByEncounterId(returnedEncounterId)
			returnedEncounter.setUsed(true)
			returnedEncounter.save(flush:true)
		}
	}
	
	def processEncounters(encountersDataParam, personId){
		//mockEncounters()
		persistEncounters(encountersDataParam, personId)
	}
	
	def persistEncounters(String encountersDataParam, personId){
		try{
			String [] encounterRecordsArray=encountersDataParam.split(RECORD_DELIM)
			for(int i=0; i<encounterRecordsArray.length; i++){
				String[] encounterPropertiesArray = encounterRecordsArray[i].split(PROP_DELIM)
				
				//encounterId
				String encounterId = encounterPropertiesArray[0].split(":")[1]
				Encounter encounter = Encounter.findByEncounterId(encounterId)
				if(!encounter){//Encounter is not allready present. Create a new one and save it.
					//startDate
					def startDate = encounterPropertiesArray[1].split(":")[1]	
					//endDate
					def endDate = encounterPropertiesArray[2].split(":")[1]
					//type
					def encounterType = encounterPropertiesArray[3].split(":")[1]
					//encounter
					encounter = createEncounter(encounterId, startDate, endDate, encounterType, personId)
				}
			}
		}catch(ArrayIndexOutOfBoundsException aiobe){
			log.error("Error thrown parsing Encounter Data from forwarding mPage.")
		}catch(Exception e){
			log.error("Error thrown persisting encounter information")
			log.error(e.getMessage())
		}
	}
	
	def createEncounter(encounterId, startDate, endDate, encounterTypeClassValue, personId){
		def encounterTypeClassDisplay= EncountersCall.encounterClassMap.get(encounterTypeClassValue)
		if(encounterTypeClassDisplay==null) encounterTypeClassDisplay="Outpatient"
		
		def encounterTitle = EncountersCall.encounterTitleMap.get(encounterTypeClassDisplay)
		if(encounterTitle==null) encounterTitle = "Ambulatory encounter"
		
		def encounterResource = EncountersCall.encounterResourceMap.get(encounterTypeClassDisplay)
		if(encounterResource==null) encounterResource = "ambulatory"
		
		def code = Code.findByTitle(encounterTitle)
		if(!code){
			code = new Code(type:"EncounterType", 
				title:encounterTitle,
				system:EncountersCall.codingSystemsMap.get(EncountersCall.ENCOUNTER_TYPE_CODING_SYSTEM),
				identifier:encounterResource)
			code.save()
		}
		
		def encounterType = CodedValue.findByTitle(encounterTitle)
		if(!encounterType){
			encounterType = new CodedValue(code:code, title:encounterTitle)
			encounterType.save()
		}
		
		def encounter = new Encounter(encounterId:encounterId, startDate:startDate, endDate:endDate, patientId:personId, encounterType:encounterType)
		encounter.save()
	}
	
	def mockEncounters(){
		def encounterIdList = [36587568, 33927843, 33927846, 31170260, 31170263, 32535439]
		def code = new Code(type:"EncounterType",
				 title:"Ambulatory encounter",
				 system:"http://smartplatforms.org/terms/codes/EncounterType#",
				 identifier:"ambulatory")
		code.save()
		
		def encounterType = new CodedValue(code:code, title:"Ambulatory encounter")
		encounterType.save()
		
		def encounter = new Encounter(encounterId:"36587568", startDate:"2012-08-02T15:00:00.000+01:00", endDate:"2012-08-03T04:59:59.000+01:00", patientId:"12914237", encounterType:encounterType, used:false)
		encounter.save()
		encounter = new Encounter(encounterId:"33927843", startDate:"2011-08-18T14:06:00.000+01:00", endDate:"2011-08-19T04:59:59.000+01:00", patientId:"12914237", encounterType:encounterType, used:false)
		encounter.save()
		encounter = new Encounter(encounterId:"33927846", startDate:"2011-08-18T14:05:00.000+01:00", endDate:"2011-08-19T04:59:59.000+01:00", patientId:"12914237", encounterType:encounterType, used:false)
		encounter.save()
		encounter = new Encounter(encounterId:"31170260", startDate:"2011-02-24T15:12:00.000Z", endDate:"2011-02-25T04:59:59.000Z", patientId:"12914237", encounterType:encounterType, used:false)
		encounter.save()
		encounter = new Encounter(encounterId:"31170263", startDate:"2011-02-24T15:11:00.000Z", endDate:"2011-02-25T04:59:59.000Z", patientId:"12914237", encounterType:encounterType, used:false)
		encounter.save()
		encounter = new Encounter(encounterId:"32535439", startDate:"2010-12-30T13:44:00.000Z", endDate:"2010-12-31T04:59:59.000Z", patientId:"12914237", encounterType:encounterType, used:false)
		encounter.save(flush:true)
	}
}
