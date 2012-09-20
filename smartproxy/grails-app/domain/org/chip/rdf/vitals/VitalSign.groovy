package org.chip.rdf.vitals

class VitalSign {
	
	public VitalSign(){
		vitalName = new CodedValue()
	}
	
	String value
	String unit
	CodedValue vitalName
	
	static mapping = {
		version false
	}
}
