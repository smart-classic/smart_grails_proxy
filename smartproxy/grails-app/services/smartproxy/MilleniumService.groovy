package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import groovy.xml.MarkupBuilder;
import groovyx.net.http.*
class MilleniumService {

    static transactional = true

	/**
     * entry method into this service class. Makes calls using the MO xml/http API
     * @param payload
     * @param 
     * @return
     */
	def makeCall(transaction, recordId) {
		def moCallObj = createMOCall(transaction)
		def requestXML = moCallObj.createRequest(recordId)
		
		//TODO:validate xml request against schema
		def moURL = ConfigurationHolder.config.grails.moURL
		def restClient = new RESTClient(moURL+moCallObj.targetServlet)
		restClient.setContentType(ContentType.XML)
		def resp=restClient.post(body:requestXML, requestContentType : ContentType.XML)
		
		moCallObj.readResponse(readData(resp))
    }
	
	/**
	 * Factory which instantiates the appropriate MilleniumObject using reflection
	 * @param transaction
	 * @return
	 */
	def createMOCall(transaction){
		transaction = upperCaseFirstLetter(transaction)

		Class moCallClass = this.class.classLoader.loadClass('org.chip.mo.'+transaction+'Call')
		def moCallObj=moCallClass.newInstance()
		moCallObj.init()
		
		return moCallObj
	}
	
	/**
	 * Takes in a string and converts the first character to uppercase
	 * @param string
	 * @return
	 */
	def upperCaseFirstLetter(string){
		String firstLetter = string.substring(0,1)
		String upperCaseFirstLetter = firstLetter.toUpperCase()
		string = string.replaceFirst(firstLetter, upperCaseFirstLetter)
		return string
	}
	
	def readData(resp){
		def replyMessage = resp.getData()
		return replyMessage.Payload
	}
	
}
