package smartproxy

class RecordsController {

    def index = { }
	
	def milleniumService
	def rdfService
	
	def makeCall={
		def recordId=params['record_id']
		def transaction = params['transaction']
		
		def record = milleniumService.makeCall(transaction, recordId)
		
		response.setContentType ('application/xml')
		response.outputStream << record.toRDF()
	}
}
