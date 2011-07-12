package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import groovy.xml.MarkupBuilder;
import groovyx.net.http.*
class MilleniumService {

    static transactional = true
	
	static Map requestMOMap = new HashMap()
	
	static {
		requestMOMap.put 'demographics', 'Demographics'
		requestMOMap.put 'problems', 'Problems'
		requestMOMap.put 'vital_signs', 'Vitals'
	}

	/**
     * entry method into this service class. Makes calls using the MO xml/http API
     * @param payload
     * @param 
     * @return
     */
	def makeCall(transaction, recordId) {
		def moCallObj = createMOCall(transaction)
		
		//TODO:validate xml request against schema
		
		def moURL = ConfigurationHolder.config.grails.moURL
		
		moCallObj.makeCall(recordId, moURL)
    }
	
	/**
	 * Factory which instantiates the appropriate MilleniumObject using reflection
	 * @param transaction
	 * @return
	 */
	def createMOCall(transaction){
		transaction = mapRequest(transaction)

		Class moCallClass = this.class.classLoader.loadClass('org.chip.mo.'+transaction+'Call')
		def moCallObj=moCallClass.newInstance()
		moCallObj.init()
		
		return moCallObj
	}
	
	/**
	 * Takes in the incoming request parameter and converts it into a transaction
	 * @param string
	 * @return
	 */
	def mapRequest(transaction){
		return requestMOMap.get(transaction)
	}
	
}
