package smartproxy

import org.chip.mo.exceptions.MOCallException;

/**
* SmartController.groovy
* Purpose: Handles the incoming requests from PowerChart (forwarding mPage)
* @author mkapoor
* @version Jun 19, 2012 12:53:03 PM
*/
class SmartController {

    def forwardService
    def casVerificationService
	def encounterService
	
	/**
	 * Records the supplied context variables.
	 * Validates the incoming token (in the request from PowerChart) using the casVerificationService.
	 * Fetches a SMART url to forward the browser to using the forwardService
	 */
    def readContext = {
		switch(request.method){
			case "HEAD":
				render"SUCCESS"
				break
			case "GET":
				def personId=params['record_id']
				def encounterId=params['encounter_id']
				def userId=params['user_id']
				def locationValue=params['location_value']
				def application=params['application']	
				def domain=params['domain']
				def casToken=params['cas_token']
				def initialApp=params['initial_app']
		
				def forwardToURL
		
				def failureReason=""
				def rootCause=""
		
				try{
					if (!casVerificationService.verifyCasToken(casToken)){
						throw new Exception("No valid CAS token supplied")
					}
					forwardToURL = forwardService.createURL(personId, domain, initialApp)
				}catch(MOCallException moce){
					failureReason=moce.getExceptionMessage()
					rootCause=moce.getRootCause()
					render(view:"error", model:[failureReason:failureReason, rootCause:rootCause])
					return
				}catch(Exception e){
					failureReason=e.getMessage()
					rootCause=e.getCause()?.getMessage()
					rootCause=(rootCause==null?"":rootCause)
					render(view:"error", model:[failureReason:failureReason, rootCause:rootCause])
					return
				}
				
				redirect(url:forwardToURL)
				break
		}
	}
	
	def readEncounters = {
		switch(request.method){
			case "POST":
				def personId=params['record_id']
				def encounterId=params['encounter_id']
				encounterService.processEncounters(personId)
		}
	}
}