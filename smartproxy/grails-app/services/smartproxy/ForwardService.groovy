package smartproxy

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.RESTClient;

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
	
    def createURL(personId, domain, initialApp) {
		personId = extractId(personId)
        initialApp = initialApp ? ("?initial_app="+initialApp) : ""

		if(domain=='demo' || domain==null){
			personId=mapPersonId(personId)
		}

        def token = ConfigurationHolder.config.oauth.smart_emr.token
        def secret= ConfigurationHolder.config.oauth.smart_emr.secret
        def api_base= ConfigurationHolder.config.oauth.smart_emr.api_base
        def smart_client= new RESTClient(api_base)

        // Instantiate as a consumer for 2-legged OAuth calls (no access tokens)
        smart_client.auth.oauth(token, secret, "", "");


        def created = smart_client.post(path: "/records/create/proxied",
                                        body : [record_id:personId,
                                        record_name:"Fake Name"], // TODO: obtain name
                                        requestContentType : URLENC )
        assert created.status == 200

        def get_url = smart_client.get(path:"/records/"+personId+"/generate_direct_url")
        assert get_url.status == 200

        def forwardToURL = get_url.data.readLine() + initial_app
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



}
