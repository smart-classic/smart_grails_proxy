package smartproxy

import org.chip.rdf.Demographics;
import org.chip.rdf.vitals.Code
import org.chip.rdf.vitals.CodedValue
import org.chip.rdf.vitals.Encounter;

class EncounterService {

    static transactional = true

    def getEncountersForPatient(patientId) {
		return Encounter.findAllByPatientId(patientId)
    }
	
	def getEncounterIdsForPatient(patientId){
		def encounters = getEncountersForPatient(patientId)
		Set encounterIds = new HashSet()
		encounters.each{encounter->
			encounterIds.add(encounter.encounterId)
		}
		return encounterIds
	}
	
	def getEncountersByIdForPatient(patientId){
		def encounters = getEncountersForPatient(patientId)
		Map encountersById = new HashMap()
		encounters.each{encounter ->
			encountersById.put(encounter.encounterId, encounter)
		}
		return encountersById
	}
	
	def processEncounters(personId){
		mockEncounters()
		//persistEncounters(encounterList, personId)
	}
	
	def persistEncounters(encounterList, personId){
		encounterList.each{encounter->
			def encounterIn = new Encounter(patientId:personId)
			encounterIn.save()
		}
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
		
		def encounter = new Encounter(encounterId:"36587568", startDate:"2012-08-02T15:00:00.000+01:00", endDate:"2012-08-03T04:59:59.000+01:00", patientId:"12914237", encounterType:encounterType)
		encounter.save()
		encounter = new Encounter(encounterId:"33927843", startDate:"2011-08-18T14:06:00.000+01:00", endDate:"2011-08-19T04:59:59.000+01:00", patientId:"12914237", encounterType:encounterType)
		encounter.save()
		encounter = new Encounter(encounterId:"33927846", startDate:"2011-08-18T14:05:00.000+01:00", endDate:"2011-08-19T04:59:59.000+01:00", patientId:"12914237", encounterType:encounterType)
		encounter.save()
		encounter = new Encounter(encounterId:"31170260", startDate:"2011-02-24T15:12:00.000Z", endDate:"2011-02-25T04:59:59.000Z", patientId:"12914237", encounterType:encounterType)
		encounter.save()
		encounter = new Encounter(encounterId:"31170263", startDate:"2011-02-24T15:11:00.000Z", endDate:"2011-02-25T04:59:59.000Z", patientId:"12914237", encounterType:encounterType)
		encounter.save()
		encounter = new Encounter(encounterId:"32535439", startDate:"2010-12-30T13:44:00.000Z", endDate:"2010-12-31T04:59:59.000Z", patientId:"12914237", encounterType:encounterType)
		encounter.save(flush:true)
	}
}
