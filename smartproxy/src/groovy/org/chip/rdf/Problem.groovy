package org.chip.rdf;

import groovy.xml.StreamingMarkupBuilder

public class Problem {
	
	public Problem(String snomedConcept, String title, String onsetDate, String resolutionDate){
		this.snomedConcept = snomedConcept;
		this.title = title;
		this.onsetDate = onsetDate;
		this.resolutionDate = resolutionDate; 
	}
	
	public String getSnomedConcept() {
		return snomedConcept;
	}
	public void setSnomedConcept(String snomedConcept) {
		this.snomedConcept = snomedConcept;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOnsetDate() {
		return onsetDate;
	}
	public void setOnsetDate(String onsetDate) {
		this.onsetDate = onsetDate;
	}
	public String getResolutionDate() {
		return resolutionDate;
	}
	public void setResolutionDate(String resolutionDate) {
		this.resolutionDate = resolutionDate;
	}
	private String snomedConcept;
	private String title;
	private String onsetDate;
	private String resolutionDate;
	
	/**
	* Performs the actual RDF generation from a Problems object
	* @param demographics
	* @return
	*/
	def toRDF(){
		Map codesMap = new HashMap()
		def builder = new StreamingMarkupBuilder()
		builder.encoding="UTF-8"
		def rdfBuilder = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(rdf:'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('dcterms':'http://purl.org/dc/terms/')
			'rdf:RDF'(){
					'sp:Problem'(){
						'sp:problemName'(){
							'sp:CodedValue'(){
								'sp:code'('rdf:resource':SNOMED_RESOURCE+this.snomedConcept)
								codesMap.put(this.snomedConcept, SNOMED_RESOURCE)
								'dcterms:title'(this.title)
							}
						}
						'sp:onset'(this.onsetDate)
						'sp:resolution'(this.resolutionDate)
					}
				codesMap.each{key, value->
					'sp:Code'('rdf:about':value+key){
						'sp:system'('rdf:resource':value)
						'dcterms:identifier'(key)
					}
				}
			}
		}
		
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}
}
