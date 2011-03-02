package smartproxy

import org.chip.rdf.*
import groovy.xml.StreamingMarkupBuilder

class RdfService {

    static transactional = true

	//TODO:write an entry point method first, then think about refactoring using inheritence.
    def convert(type, moResponse) {
		if (type=='demographics'){
			generateDemographicsRDF(createDemographics (moResponse))	
		}
    }
	
	//TODO: use xml binding here
	/**
	 * Reads in the MO response and converts it to a Demographics object
	 * @param moResponse
	 * @return
	 */
	def createDemographics(moResponse){
		def person = moResponse.Person
		def birthDateTime=person.BirthDateTime.text().substring(0, 10)
		def givenName=person.FirstName.text()
		def familyName=person.LastName.text()
		def gender=person.Gender.Meaning.text()
		def zipcode=person.Addresses.Address.Zipcode.text().substring(0,5)
		return new Demographics(birthDateTime, givenName, familyName, gender, zipcode)
	}
	
	//TODO: user rdf binding here
	/**
	 * Performs the actual RDF generation from a demographics object
	 * @param demographics
	 * @return
	 */
	def generateDemographicsRDF(demographics){
		def builder = new StreamingMarkupBuilder()
		builder.encoding="UTF-8"
		def rdfBuilder = {
			mkp.xmlDeclaration()
			mkp.declareNamespace(rdf:'http://www.w3.org/1999/02/22-rdf-syntax-ns#')
			mkp.declareNamespace('sp':'http://smartplatforms.org/terms#')
			mkp.declareNamespace('foaf':'http://xmlns.com/foaf/0.1/')
			'rdf:RDF'(){
				'foaf:Person'(){
					'foaf:givenName'(demographics.getGivenName())
					'foaf:familyName'(demographics.getFamilyName())
					'foaf:gender'(demographics.getGender())
					'sp:zipcode'(demographics.getZipcode())
					'sp:birthday'(demographics.getBirthDateTime())
				}
			}
		}
		
		def writer = new StringWriter()
		writer<<builder.bind(rdfBuilder)
		writer.toString()
	}
	
	//TODO:validate RDF request against RDF schema
	def rdfOut='<?xml version="1.0" encoding="utf-8"?><rdf:RDF  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"  xmlns:sp="http://smartplatforms.org/terms#"  xmlns:foaf="http://xmlns.com/foaf/0.1/">   <foaf:Person>     <foaf:givenName>Bob</foaf:givenName>     <foaf:familyName>Odenkirk</foaf:familyName>     <foaf:gender>male</foaf:gender>     <sp:zipcode>90001</sp:zipcode>     <sp:birthday>1959-12-25</sp:birthday>   </foaf:Person></rdf:RDF>'
}
