package smartproxy

import org.chip.mo.exceptions.MOCallException;

class SmartController {

    def forwardService
    def casVerificationService

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
}