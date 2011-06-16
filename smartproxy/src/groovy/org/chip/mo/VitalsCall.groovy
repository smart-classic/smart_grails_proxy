package org.chip.mo;

import org.chip.rdf.Vitals;

class VitalsCall extends MilleniumObjectCall{
	
	def init(){
		super.init()
		transaction = 'ReadResultsMatrix'
		targetServlet = 'com.cerner.results.ResultsServlet'
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

	  return new Vitals()
  }
}
