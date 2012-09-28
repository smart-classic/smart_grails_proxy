package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.chip.mo.exceptions.MOCallException

import groovy.xml.MarkupBuilder;
import groovyx.net.http.*

/**
 * MilleniumService.groovy
 * Purpose: Provides functionality to initialize and make calls using Millennium Object API.
 * @author mkapoor
 * @version Jun 19, 2012 12:53:03 PM
 */
class MilleniumService {

    static transactional = false
	
	/**
	 * Maps the incoming transaction to a list of resolving vital calls.
	 * The vital_signs call can include a 'Encounters' call which precedes the 'Results' call.
	 * Disabled now because encounter information is sent in by the forwarding mPage and then read from the db.
	 */
	static Map transactionStepsMap = new HashMap()
	
	static {
		transactionStepsMap.put 'demographics', ['Demographics']
		transactionStepsMap.put 'problems', ['Problems']
		transactionStepsMap.put 'vital_signs', ['Results']
	}

	/**
	 * Entry method into this service class.
	 * Gets transaction specific instances of the MilleniumObject Call class
	 * Invokes makeCall on the list of MO Call objects, one at a time.
	 * It also passes the response from a MO call to the makeCall method of the next call.
	 * The response returned by the last call is returned.
	 *
	 * @param transaction the transaction
	 * @param recordId the record id
	 * @return the java.lang. object
	 * @throws MOCallException the mO call exception
	 */
	def makeCall(transaction, recordId, requestParams) throws MOCallException {
		if((recordId==null)||(recordId.trim().length()==0)){
			throw new MOCallException("Record ID not specified", 400, "")
		}
		def moCalls = createMOCalls(transaction, requestParams)
		def moURL = ConfigurationHolder.config.grails.moURL
		
		def moResponse=null
		moCalls.each{moCall->
			moResponse = moCall.makeCall(recordId, moURL, moResponse)
		}
		return moResponse
    }
	
	def makeCall(transaction, recordId){
		makeCall(transaction, recordId, null)
	}
	
	/**
	 * Factory which instantiates the list of appropriate MilleniumObject using reflection.
	 *
	 * @param transaction the transaction
	 * @return the java.lang. object
	 * @throws MOCallException the mO call exception
	 */
	def createMOCalls(transaction, requestParams) throws MOCallException{
		def moCalls
		try{
			moCalls = new ArrayList()
			transactionStepsMap.get(transaction).each{transactionStep ->
				Class moCallClass = this.class.classLoader.loadClass('org.chip.mo.'+transactionStep+'Call')
				def moCallObj=moCallClass.newInstance()
				moCallObj.init()
				moCallObj.registerRequestParams(requestParams)
				moCalls.add(moCallObj)
			}
			

		}catch(Exception e){
			throw new MOCallException("Transaction \""+transaction+"\" not implemented", 501, e.getMessage())
		}
		return moCalls
	}
}
