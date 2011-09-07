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
		
		if(record=="Error"){
			response.setStatus(404)
			response.outputStream << "<Response>Error</Response>"
		}else{
			response.outputStream << record.toRDF()
		}
	}
}
