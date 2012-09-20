package org.chip.rdf.vitals

class CodedValue {
	
	public CodedValue(){
		code = new Code()
	}
	
	Code code
	String title
	
	static mapping = {
		version false
		id generator: 'assigned', name: "title", type: 'string'
	}
	
}
