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
	
	def clean = {
		encounterId = ""
		eventCode = ""
		value = ""
		eventId = ""
		parentEventId = ""
		eventEndDateTime = ""
		updateDateTime = ""
		eventTag = ""
	}

}
