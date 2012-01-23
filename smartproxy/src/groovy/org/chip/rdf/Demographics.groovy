package org.chip.rdf;

import groovy.xml.StreamingMarkupBuilder

public class Demographics extends Record {
	
	public static final String MRN_SYSTEM='CHB'
	
	public Demographics(String birthDateTime, String givenName, String familyName, String gender, String zipcode, String mrn){
		this.birthDateTime=birthDateTime;
		this.givenName=givenName;
		this.familyName=familyName;
		this.gender=gender;
		this.zipcode=zipcode;
		this.mrn=mrn;
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
	private String mrn;
	
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
			mkp.declareNamespace('rdf':'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('foaf':'http://xmlns.com/foaf/0.1/')
			mkp.declareNamespace('v':'http://www.w3.org/2006/vcard/ns#')
			mkp.declareNamespace('dcterms':'http://purl.org/dc/terms/')
			'rdf:RDF'(){
				'sp:Demographics'(){
					'v:n'() {
						'v:Name'() {
							'v:given-name'(this.getGivenName())
							'v:family-name'(this.getFamilyName())
						}
					}

					'v:adr'() {
						'v:Address'() {
							'v:postal-code'(this.getZipcode())
						}
					}
					'foaf:gender'(this.getGender())
					if(this.getBirthDateTime().length()>0){
					'v:bday'(this.getBirthDateTime())
					}
					'sp:medicalRecordNumber'(){
						'sp:Code'(){
							'dcterms:title'(MRN_SYSTEM+' '+this.mrn)
							'dcterms:identifier'(this.mrn)
							'sp:system'(MRN_SYSTEM)
						}
					}
				}
			}
		}
		
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}
	
}
