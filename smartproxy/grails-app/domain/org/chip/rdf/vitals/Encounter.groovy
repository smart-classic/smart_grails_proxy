package org.chip.rdf.vitals

class Encounter {
	public Encounter(){
		encounterType = new CodedValue()
	}
	String encounterId
	String startDate
	String endDate
	CodedValue encounterType
	
	String patientId
	
	static mapping = {
		id generator: 'assigned', name: "encounterId", type: 'string'
	}
	
}
