package smartproxy

class CasVerificationService {

    static transactional = true

    def verifyCasToken(casToken) {
        //TODO: implement token verification via CHB server
        return casToken == "fake_token_value"
    }
}
