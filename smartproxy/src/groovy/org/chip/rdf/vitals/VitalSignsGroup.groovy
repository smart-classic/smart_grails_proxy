package org.chip.rdf.vitals

class VitalSignsGroup {
	String date
	Map<String, List<VitalSign>> vitalSignsByParentEvent
	Encounter encounter
	
	public VitalSignsGroup(){
		vitalSignsByParentEvent = new HashMap()
	}
	
	public Encounter getEncounter(){
		return encounter
	}
}
