package smartproxy

class SmartController {

	def forwardService
	
    def readContext = {
		def personId=params['record_id']
		def encounterId=params['encounter_id']
		def userId=params['user_id']
		def locationValue=params['location_value']
		def application=params['application']	
		def domain=params['domain']
		
		
		def forwardToURL = forwardService.createURL(personId, encounterId, userId, locationValue, application, domain)
		redirect(url:forwardToURL)
	}
}
