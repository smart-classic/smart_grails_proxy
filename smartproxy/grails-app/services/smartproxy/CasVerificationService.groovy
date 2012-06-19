package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.RESTClient;
import static groovyx.net.http.ContentType.XML


class CasVerificationService {

    static transactional = false

    def verifyCasToken(casToken) {

        if (ConfigurationHolder.config.cas.chb.skipValidation)
           	return true;

        def casValidationUrl = ConfigurationHolder.config.cas.chb.casValidationUrl
		def casClientId = ConfigurationHolder.config.cas.chb.casClientId
		def casServiceId = ConfigurationHolder.config.cas.chb.casServiceId

		def authorized
		try{
			def casClient= new RESTClient(casValidationUrl)
			casClient.setContentType(XML)

			authorized = casClient.get( query:[ticket:casToken, clientId:casClientId, serviceId:casServiceId])
			
		}catch(URISyntaxException urise){
			throw new Exception("Invalid CAS server URI: "+ casValidationUrl, urise)
		}catch(IOException ioe){
			throw new Exception("Error communicating with CAS server at: "+ casValidationUrl, ioe)
		}

		if (authorized.status!=200){
			log.error("CAS Server Error at: "+casServiceId)
			throw new Exception("CAS Server Error")
		}
		
        def authMsg = authorized.responseData
        authMsg.declareNamespace(cas:"http://www.yale.edu/tp/cas")

        def success = authMsg.'cas:authenticationSuccess'.size() == 1
        return success
    }
}