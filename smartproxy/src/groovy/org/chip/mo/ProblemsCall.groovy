package org.chip.mo

import org.chip.rdf.Problem

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
   def generatePayload(recordId){
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
	   def problems = new ArrayList()
	   def help = moResponse.Problems.Problem.Nomenclature.SourceVocabulary.Value.text()
	   moResponse.Problems.Problem.each{
		   def snomedConcept = it.Nomenclature.SourceVocabulary.Value.text()
		   def title = it.Nomenclature.SourceString.text()
		   def onsetDate = it.OnsetDateTime.text()
		   if(onsetDate.size()>0){
			   onsetDate=onsetDate.substring(0, 10)
		   }
		   def resolutionDate = it.ActualResolutionDateTime.text()
		   if(resolutionDate.size()>0){
			   resolutionDate=resolutionDate.substring(0, 10)
		   }
		   problems.add(new Problem(snomedConcept, title, onsetDate, resolutionDate) )
	   }
	   return problems
   }
}
