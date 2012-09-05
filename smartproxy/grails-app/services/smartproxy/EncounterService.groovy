package smartproxy

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
}
