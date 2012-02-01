package org.chip.mo

import org.chip.mo.exceptions.MOCallException;
import org.chip.rdf.Demographics

class DemographicsCall extends MilleniumObjectCall{
	
	public static final String PERSON_ALIAS_TYPE_MEANING_MRN="MRN"
	public static final String PERSON_ALIAS_POOL_VALUE_CHB_MRN="3110551"
	
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
   def generatePayload(requestParams){
	   def recordId = (String)requestParams.get(RECORDIDPARAM)
	   builder.PersonId(recordId)
	   builder.AddressesIndicator('true')
	   builder.PersonAliasesIndicator('true') 
   }
   
   /**
   * Reads in the MO response and converts it to a Demographics object
   * @param moResponse
   * @return
   */
  def readResponse(moResponse)throws MOCallException{
	  def birthDateTime=""
	  def givenName=""
	  def familyName=""
	  def gender=""
	  def zipcode=""
	  def mrn=""
	  try{
		  def replyMessage = moResponse.getData()
		  def payload= replyMessage.Payload
		  
		  def person = payload.Person
		   birthDateTime = person.BirthDateTime.text()
		  if(birthDateTime.length()>0){
			  birthDateTime=person.BirthDateTime.text().substring(0, 10)
		  }
		   givenName=person.FirstName.text()
		   familyName=person.LastName.text()
		   gender=person.Gender.Meaning.text().toLowerCase()
		   zipcode=""
		  if(person.Addresses.Address.Zipcode.text().length()>=5){
			  zipcode=person.Addresses.Address.Zipcode.text().substring(0,5)
		  }
		   mrn=""
		  def personalAliases = person.PersonAliases
		  person.PersonAliases.PersonAlias.each{ personAlias->
			   if((personAlias.AliasPool.Value.text()==PERSON_ALIAS_POOL_VALUE_CHB_MRN)
				   &&(personAlias.PersonAliasType.Meaning.text()==PERSON_ALIAS_TYPE_MEANING_MRN)){
				  mrn = personAlias.Alias.text()
			  }
		  }		
		 }catch(Exception e){
			throw new MOCallException("Error reading MO response", 500, e.getMessage())
		}
	  return new Demographics(birthDateTime, givenName, familyName, gender, zipcode, mrn)
  }
}
