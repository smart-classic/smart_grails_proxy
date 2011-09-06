package smartproxy
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.RESTClient;
import static groovyx.net.http.ContentType.XML


class CasVerificationService {

    static transactional = true

    def verifyCasToken(casToken) {

        return true //TODO: remove this line to provide token verification via CHB server

        def cas_validation_url = ConfigurationHolder.config.cas.chb.validation_url
        def cas_service_url = ConfigurationHolder.config.cas.chb.service_url

        def cas_client= new RESTClient(cas_validation_url)
        cas_client.setContentType(XML)

        def authorized = cas_client.get( query:[ticket:casToken, service:cas_service_url])
        assert authorized.status==200

        def auth_msg = authorized.responseData
        auth_msg.declareNamespace(cas:"http://www.yale.edu/tp/cas")

        def success = auth_msg.'cas:authenticationSuccessfail'.size() == 1
        return success
    }
}
