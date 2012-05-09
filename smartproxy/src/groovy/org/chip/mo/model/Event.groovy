package org.chip.mo.model

class Event {
	
	def encounterId
	def parentEventId
	def eventEndDateTime
	def updateDateTime
	
	def eventId
	
	def eventCode
	def value
	def eventTag
	
	def recordId
	
	public String toString(){
		String ret=""
		this.properties.each{property, value->
			ret+=property+":"+value+", "
		}
		return ret 
	}
}
