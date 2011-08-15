package org.chip.rdf.vitals

class VitalSigns {
	String date
	Map<String, List<VitalSign>> vitalSignMap
	Encounter encounter
	
	public VitalSigns(){
		vitalSignMap = new HashMap()
	}
	
	public Encounter getEncounter(){
		return encounter
	}
}
