package org.chip.rdf.vitals

class Code {
	String type
	String title
	String system
	String identifier
	
	static mapping = {
		version false
		id generator: 'assigned', name: "title", type: 'string'
	}
}
