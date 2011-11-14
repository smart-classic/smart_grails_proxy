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

		
        def casClient= new RESTClient(casValidationUrl)
        casClient.setContentType(XML)

        def authorized = casClient.get( query:[ticket:casToken, clientId:casClientId, serviceId:casServiceId])
        assert authorized.status==200

        def authMsg = authorized.responseData
        authMsg.declareNamespace(cas:"http://www.yale.edu/tp/cas")

        def success = authMsg.'cas:authenticationSuccess'.size() == 1
        return success
    }
}