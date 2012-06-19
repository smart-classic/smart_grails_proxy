package smartproxy

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
		
				assert casVerificationService.verifyCasToken(casToken)
		
				def forwardToURL = forwardService.createURL(personId, domain, initialApp)
				redirect(url:forwardToURL)
				break
		}
	}
}
