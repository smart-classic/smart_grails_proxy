package org.chip.mo

import org.chip.rdf.Demographics

class DemographicsCall extends MilleniumObjectCall{
	
	def makeCall(recordId, moURL){
		createRequest(recordId)
	}
	
	def init(){
		super.init()
		transaction = 'ReadPersonById'
		targetServlet = 'com.cerner.person.PersonServlet'
	}
	
	/**
	* Generate appropriate MO request xml payload
	* @param recordId
	* @return
	*/
   def generatePayload(recordId){
	   builder.PersonId(recordId)
	   builder.AddressesIndicator('true')
   }
   
   /**
   * Reads in the MO response and converts it to a Demographics object
   * @param moResponse
   * @return
   */
  def readResponse(moResponse){
	  
	  def replyMessage = moResponse.getData()
	  def payload= replyMessage.Payload
	  
	  def person = payload.Person
	  def birthDateTime=person.BirthDateTime.text().substring(0, 10)
	  def givenName=person.FirstName.text()
	  def familyName=person.LastName.text()
	  def gender=person.Gender.Meaning.text().toLowerCase()
	  def zipcode=person.Addresses.Address.Zipcode.text().substring(0,5)
	  return new Demographics(birthDateTime, givenName, familyName, gender, zipcode)
  }
}
