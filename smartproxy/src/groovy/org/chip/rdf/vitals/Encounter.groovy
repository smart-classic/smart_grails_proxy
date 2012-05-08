package org.chip.rdf.vitals

class Encounter {
	public Encounter(){
		encounterType = new CodedValue()
	}
	
	String startDate
	String endDate
	CodedValue encounterType
	
	String belongsTo
}
