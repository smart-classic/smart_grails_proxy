package org.chip.rdf

abstract class Record {

	private static final String SNOMED_RESOURCE = 'http://www.ihtsdo.org/snomed-ct/concepts/'
	
	def abstract toRDF()
}
