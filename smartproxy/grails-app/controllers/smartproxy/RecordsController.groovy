package smartproxy
import org.chip.mo.exceptions.MOCallException;
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
		def record
		def rdf
		response.setContentType ('application/xml')
		try{
			record = milleniumService.makeCall(transaction, recordId)
		}catch(MOCallException moce){
			log.error(moce.getExceptionMessage())
			log.error(moce.getRootCause())
			response.setStatus(moce.getStatusCode())
			response.outputStream << "<Error><Message>"+moce.getExceptionMessage()+"</Message><RootCause>"+moce.getRootCause()+"</RootCause></Error>"
			return;
		}
		
		try{
			rdf = record.toRDF()
		}catch(Exception e){
			log.error("Error creating RDF")
			log.error(e.getMessage())
			response.setStatus(500)
			response.outputStream << "<Error><Message>Error creating RDF</Message><RootCause>"+e.getMessage()+"</RootCause></Error>"
			return;
		}
		response.outputStream << rdf
	}
}
