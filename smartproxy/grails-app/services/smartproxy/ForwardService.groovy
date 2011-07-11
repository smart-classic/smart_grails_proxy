package smartproxy

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class ForwardService {

    static transactional = true

	final static Map personIdMap
	
	static{
		personIdMap = new HashMap()
		personIdMap.put ('14109736','15649502')
		personIdMap.put ('14109737','15649505')
		personIdMap.put ('14109738','15649508')
		personIdMap.put ('14109748','15649511')
	}
	
    def createURL(personId, encounterId, userId, locationValue, application) {
		personId = extractId(personId)
		def baseURL = ConfigurationHolder.config.grails.smartURL
		def forwardToURL = baseURL+'?record_id='+mapPersonId(personId)
		return forwardToURL
    }
	
	def mapPersonId(personId) {
		personIdMap.get personId
	}
	
	def extractId(String incomingId){
		return incomingId.substring(0, incomingId.indexOf("."))
	}
}
