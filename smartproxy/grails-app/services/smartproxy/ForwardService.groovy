package smartproxy

import org.chip.rdf.Demographics;
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.RESTClient;
import static groovyx.net.http.ContentType.URLENC

class ForwardService {

    static transactional = true

	final static Map personIdMap

	def milleniumService
		
	static{
		personIdMap = new HashMap()
		personIdMap.put ('14109736','15649502')
		personIdMap.put ('14109737','15649505')
		personIdMap.put ('14109738','15649508')
		personIdMap.put ('14109748','15649511')
	}
	
    def createURL(personId, domain, initialApp) {
		personId = extractId(personId)
        initialApp = initialApp ? ("?initial_app="+initialApp) : ""

		if(domain=='demo' || domain==null){
            if (mapPersonId(personId)) {
			    personId=mapPersonId(personId)
            }
		}

        def token = ConfigurationHolder.config.oauth.smartEmr.token
        def secret= ConfigurationHolder.config.oauth.smartEmr.secret
        def apiBase= ConfigurationHolder.config.oauth.smartEmr.apiBase
        def smartClient= new RESTClient(apiBase)

        // Instantiate as a consumer for 2-legged OAuth calls (no access tokens)
        smartClient.auth.oauth(token, secret, "", "");


        def created = smartClient.post(path: "/records/create/proxied",
                                        body : [record_id:personId,
                                        record_name:getNameForPersonId(personId)],
                                        requestContentType : URLENC )
        assert created.status == 200

        def getUrl = smartClient.get(path:"/records/"+personId+"/generate_direct_url")
        assert getUrl.status == 200

        def forwardToURL = getUrl.data.readLine() + initialApp
		return forwardToURL
    }
	
	def mapPersonId(personId) {
		personIdMap.get personId
	}
	
	def extractId(String incomingId){
		def index = incomingId.indexOf(".")
		if (index >-1){
			return incomingId.substring(0, index)
		}
		return incomingId
	}

	def getNameForPersonId(personId){
		Demographics  demographics = milleniumService.makeCall('demographics', personId)
		return demographics.getGivenName()+" "+demographics.getFamilyName()
	}

}
