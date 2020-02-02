package com.cogent.cogentappointment.client.security.hmac;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Getter
@Setter
public class AuthHeader {

    private final String algorithm;
    private final String username;
    private final String hospitalCode;
    private final String apiKey;
    private final String nonce;
    private final byte[] digest;

    public AuthHeader(String algorithm, String username, String hospitalCode, String apiKey, String nonce, byte[] digest) {
        this.algorithm = algorithm;
        this.username = username;
        this.hospitalCode = hospitalCode;
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
    }
}
