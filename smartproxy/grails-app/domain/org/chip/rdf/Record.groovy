package org.chip.rdf

abstract class Record {

	private static final String SNOMED_RESOURCE = 'http://purl.bioontology.org/ontology/SNOMEDCT/'
	
	def abstract toRDF()
}
