package org.chip.mo

import org.chip.mo.exceptions.MOCallException;
import org.chip.rdf.Problem

/**
* ProblemsCall.groovy
* Purpose: Represents the Millennium Object call to filter and return Problems information for a specific Person id.
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class ProblemsCall extends MilleniumObjectCall {

   def init(){
	   super.init()
	   transaction='ReadProblemsByPersonIds'
	   targetServlet='com.cerner.problem.ProblemServlet'
   }
   
   /**
   * Generates MO request to get Problems for a given Person ID
   * @param recordId
   * @return
   */
   def generatePayload(){
	   def recordId = (String)requestParams.get(RECORDIDPARAM)
	   builder.CanceledIndicator('All')
	   builder.PersonIds(){
		   PersonId(recordId)
	   }
   }
   
   /**
   *Reads in the MO response and converts it to a Problems object
   * @param moResponse
   * @return
   */
   def readResponse(moResponse){
	   
	   def replyMessage = moResponse.getData()
	   def payload= replyMessage.Payload
	   
	   def problems = new ArrayList()
	   try{
		   def help = payload.Problems.Problem.Nomenclature.SourceVocabulary.Value.text()
		   payload.Problems.Problem.each{
			   def snomedConcept = it.Nomenclature.SourceVocabulary.Value.text()
			   def title = it.Nomenclature.SourceString.text()
			   def onsetDate = it.OnsetDateTime.text()
			   def resolutionDate = it.ActualResolutionDateTime.text()
			   problems.add(new Problem(snomedConcept, title, onsetDate, resolutionDate) )
		   }
	   }catch(Exception e){
	   		throw new MOCallException("Error reading MO response for Problems", 500, e.getMessage())
	   }
	   return problems
   }
}
