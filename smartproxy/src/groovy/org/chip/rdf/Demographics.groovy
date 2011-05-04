package org.chip.rdf;

import groovy.xml.StreamingMarkupBuilder

public class Demographics extends Record {
	
	public Demographics(String birthDateTime, String givenName, String familyName, String gender, String zipcode){
		this.birthDateTime=birthDateTime;
		this.givenName=givenName;
		this.familyName=familyName;
		this.gender=gender;
		this.zipcode=zipcode;
	}
	
	public String getBirthDateTime() {
		return birthDateTime;
	}
	public void setBirthDateTime(String birthDateTime) {
		this.birthDateTime = birthDateTime;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	private String birthDateTime;
	private String givenName;
	private String familyName;
	private String gender;
	private String zipcode;
	
	//TODO: user rdf binding here
	/**
	 * Performs the actual RDF generation from a demographics object
	 * @param demographics
	 * @return
	 */
	def toRDF(){
		def builder = new StreamingMarkupBuilder()
		builder.encoding="UTF-8"
		def rdfBuilder = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(rdf:'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('foaf':'http://xmlns.com/foaf/0.1/')
			'rdf:RDF'(){
				'foaf:Person'(){
					'foaf:givenName'(this.getGivenName())
					'foaf:familyName'(this.getFamilyName())
					'foaf:gender'(this.getGender())
					'sp:zipcode'(this.getZipcode())
					'sp:birthday'(this.getBirthDateTime())
				}
			}
		}
		
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}
	
	def rdfOut='<?xml version="1.0" encoding="utf-8"?><rdf:RDF  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"  xmlns:sp="http://smartplatforms.org/terms#"  xmlns:foaf="http://xmlns.com/foaf/0.1/">   <foaf:Person>     <foaf:givenName>Bob</foaf:givenName>     <foaf:familyName>Odenkirk</foaf:familyName>     <foaf:gender>male</foaf:gender>     <sp:zipcode>90001</sp:zipcode>     <sp:birthday>1959-12-25</sp:birthday>   </foaf:Person></rdf:RDF>'
}
