package smartproxy

class RecordsController {

    def index = { }
	
	def milleniumService
	
	/**
	 * Reads the incoming record id and transaction.
	 * Calls the milleniumService to make the appropriate call.
	 */
	def makeCall={
		def recordId=params['record_id']
		def transaction = params['transaction']
		
		def record = milleniumService.makeCall(transaction, recordId)
		
		response.setContentType ('application/xml')
		response.outputStream << record.toRDF()
	}
}
