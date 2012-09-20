package org.chip.rdf.vitals

class VitalSigns {
	String date
	Encounter encounter
	VitalSign weight
	VitalSign height
	VitalSign bodyMassIndex
	VitalSign respiratoryRate
	VitalSign heartRate
	VitalSign oxygenSaturation
	VitalSign temperature
	BloodPressure bloodPressure
	String patientId
	
	static mapping = {
		version false
	}
	
	static constraints = {
		weight(nullable:true)
		height(nullable:true)
		bodyMassIndex(nullable:true)
		respiratoryRate(nullable:true)
		heartRate(nullable:true)
		oxygenSaturation(nullable:true)
		temperature(nullable:true)
		bloodPressure(nullable:true)
	}
}
