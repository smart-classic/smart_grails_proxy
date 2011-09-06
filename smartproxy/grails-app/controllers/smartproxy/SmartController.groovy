package smartproxy

class SmartController {

    def forwardService
    def casVerificationService

    def readContext = {
		def personId=params['record_id']
		def encounterId=params['encounter_id']
		def userId=params['user_id']
		def locationValue=params['location_value']
		def application=params['application']	
        def domain=params['domain']
        def cas_token=params['cas_token']
        def initial_app=params['initial_app']

        assert casVerificationService.verifyCasToken(cas_token)

        def forwardToURL = forwardService.createURL(personId, domain)
		redirect(url:forwardToURL)
	}
}
