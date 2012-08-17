package org.chip.mo.model

/**
* Event.groovy
* Purpose: Represents the Millenium Object provided information for an Event.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class Event {
	
	def encounterId
	def parentEventId
	def eventEndDateTime
	def updateDateTime
	
	def eventId
	
	def eventCode
	def eventValue
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
