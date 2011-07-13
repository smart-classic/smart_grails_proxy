package org.chip.rdf.vitals

class VitalSigns {
	String date
	List<VitalSign> vitalSignList
	Encounter encounter
	
	public VitalSigns(){
		vitalSignList = new ArrayList()
	}
	
	public Encounter getEncounter(){
		return encounter
	}
}
