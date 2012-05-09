package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.chip.mo.exceptions.MOCallException

import groovy.xml.MarkupBuilder;
import groovyx.net.http.*
class MilleniumService {

    static transactional = false
	
	/**
	 * Maps the incoming transaction to a list of resolving vital calls.
	 */
	static Map transactionStepsMap = new HashMap()
	
	static {
		transactionStepsMap.put 'demographics', ['Demographics']
		transactionStepsMap.put 'problems', ['Problems']
		transactionStepsMap.put 'vital_signs', ['Encounters','Results']
	}

	/**
     * Entry method into this service class.
     * Gets transaction specific instances of the MilleniumObject Call class
     * Invokes makeCall on the list of MO Call objects, one at a time.
     * It also passes the response from a MO call to the makeCall method of the next call.
     * The response returned by the last call is returned.
     * @param payload
     * @param 
     * @return
     */
	def makeCall(transaction, recordId) throws MOCallException {
		if((recordId==null)||(recordId.trim().length()==0)){
			throw new MOCallException("Record ID not specified", 400, "")
		}
		def moCalls = createMOCalls(transaction)
		def moURL = ConfigurationHolder.config.grails.moURL
		
		def moResponse=null
		moCalls.each{moCall->
			moResponse = moCall.makeCall(recordId, moURL, moResponse)
		}
		return moResponse
    }
	
	/**
	 * Factory which instantiates the list of appropriate MilleniumObject using reflection
	 * @param transaction
	 * @return
	 */
	def createMOCalls(transaction) throws MOCallException{
		def moCalls
		try{
			moCalls = new ArrayList()
			transactionStepsMap.get(transaction).each{transactionStep ->
				Class moCallClass = this.class.classLoader.loadClass('org.chip.mo.'+transactionStep+'Call')
				def moCallObj=moCallClass.newInstance()
				moCallObj.init()
				moCalls.add(moCallObj)
			}
			

		}catch(Exception e){
			throw new MOCallException("Transaction \""+transaction+"\" not implemented", 501, e.getMessage())
		}
		return moCalls
	}
}
