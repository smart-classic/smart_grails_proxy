package org.chip.mo

import org.chip.mo.exceptions.MOCallException;
import org.chip.rdf.Demographics


/**
* DemographicsCall.groovy
* Purpose: Represents the Millennium Object call to read Person information by id.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class DemographicsCall extends MilleniumObjectCall{
	
	public static final String PERSON_ALIAS_TYPE_MEANING_MRN="MRN"
	public static final String PERSON_ALIAS_POOL_DISPLAY_CHB_MRN="CHB_MRN"
	
	def init(){
		super.init()
		transaction = 'ReadPersonById'
		targetServlet = 'com.cerner.person.PersonServlet'
	}
	
	/**
	* Generate request payload to fetch person(demographics) information using the Millennium Objects API.
	* @param recordId
	* @return
	*/
   def generatePayload(){
	   def recordId = (String)requestParams.get(RECORDIDPARAM)
	   builder.PersonId(recordId)
	   builder.AddressesIndicator('true')
	   builder.PersonAliasesIndicator('true') 
   }
   
   /**
   * Parses the MO response, extracts basic demographic information from it and creates a Demographics object.
   * 
   * @param moResponse
   * @return Demographics
   */
  def readResponse(moResponse)throws MOCallException{
	  def birthDateTime=""
	  def givenName=""
	  def familyName=""
	  def gender=""
	  def zipcode=""
	  def mrn=""
	  def personId=""
	  try{
		  def replyMessage = moResponse.getData()
		  def payload= replyMessage.Payload
		  
		  def person = payload.Person
		  
		  personId=person.PersonId.text()
		  birthDateTime = person.BirthDateTime.text()
		  if(null!=birthDateTime && birthDateTime.length()>0){
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
			   if((personAlias.AliasPool.Display.text()==PERSON_ALIAS_POOL_DISPLAY_CHB_MRN)
				   &&(personAlias.PersonAliasType.Meaning.text()==PERSON_ALIAS_TYPE_MEANING_MRN)){
				  mrn = personAlias.Alias.text()
			  }
		  }		
		 }catch(Exception e){
			throw new MOCallException("Error reading MO response", 500, e.getMessage())
		}
	  return new Demographics(birthDateTime, givenName, familyName, gender, zipcode, mrn, personId)
  }
  
  def handleExceptions(resp, recordId)throws MOCallException{
	  def replyMessage = resp.getData()
	  def status= replyMessage.Status.text()
	  if( status != MO_RESP_STATUS_SUCCESS){
		  def errorMessage = responseErrorMessageMap.get(status)
		  def statusCode = responseErrorStatusCodeMap.get(status)
		  if(errorMessage==null){
			  errorMessage = "Unexpected or no response from MO "
			  statusCode = 502
		  }
		  throw new MOCallException(errorMessage+ recordId, statusCode, "MO Response returned status of "+status)
	  }
  }
}
