package org.chip.rdf.vitals

class BloodPressure {
	CodedValue bodyPosition
	CodedValue bodySite
	VitalSign diastolic
	VitalSign systolic
	CodedValue method
	
	static mapping = {
		version false
	}
	
	static constraints = {
		bodyPosition(nullable:true)
		bodySite(nullable:true)
		diastolic(nullable:true)
		systolic(nullable:true)
		method(nullable:true)
	}
}
