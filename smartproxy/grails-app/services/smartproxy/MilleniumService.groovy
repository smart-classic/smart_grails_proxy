package smartproxy
import groovy.xml.MarkupBuilder;
import groovyx.net.http.*
class MilleniumService {

    static transactional = true

    private static final transactionMap=['demographics':'ReadPersonsById']
	
	//TODO:put this in the config file
	private final moURL = 'http://soatstweb2:8888/CHMObjectsToolkit/servlet/com.cerner.person.PersonServlet'
	
	/**
     * entry method into this service class. Makes calls using the MO xml/http API
     * @param payload
     * @param 
     * @return
     */
	def makeCall(transaction, recordId) {
		//TODO:enable run time method selection based on incoming transaction.
		//TODO:write an entry point method first, then think about refactoring using inheritence.
		return personById(recordId)
    }
	
	/**
	 * makes the actual call to MO to obtain person details using there id
	 * @param recordId
	 * @return
	 */
	def personById(recordId){
		//TODO:build request xml using xml binding
		//TODO:validate xml request against schema
		def requestXML = generatePersonByIdRequest(recordId)
		
		
		def restClient = new RESTClient(moURL)
		restClient.setContentType(ContentType.XML)
		def resp=restClient.post(body:requestXML, requestContentType : ContentType.XML)
		
		//TODO: use constants to store transaction names
		readData(resp)
	}
	
	/**
	 * Generate appropriate MO request xml
	 * @param recordId
	 * @return
	 */
	def generatePersonByIdRequest(recordId){
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		
		builder.RequestMessage(){
			TransactionName('ReadPersonById')
			Payload(){
				PersonId(recordId)
				AddressesIndicator('true')
			}
		}
		
		return writer.toString()
	}
	
	
	def readData(resp){
		def replyMessage = resp.getData()
		return replyMessage.Payload
	}
}
