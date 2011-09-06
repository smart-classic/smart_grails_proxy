package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.RESTClient;
import static groovyx.net.http.ContentType.XML


class CasVerificationService {

    static transactional = true

    def verifyCasToken(casToken) {

        if (ConfigurationHolder.config.cas.chb.skipValidation)
            return true;

        def casValidationUrl = ConfigurationHolder.config.cas.chb.validationUrl
        def casServiceUrl = ConfigurationHolder.config.cas.chb.serviceUrl

        def casClient= new RESTClient(casValidationUrl)
        casClient.setContentType(XML)

        def authorized = casClient.get( query:[ticket:casToken, service:casServiceUrl])
        assert authorized.status==200

        def authMsg = authorized.responseData
        authMsg.declareNamespace(cas:"http://www.yale.edu/tp/cas")

        def success = authMsg.'cas:authenticationSuccess'.size() == 1
        return success
    }
}