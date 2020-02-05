package com.cogent.cogentappointment.admin.security.hmac;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Getter
public class AuthHeader {

    private final String algorithm;
    private final String apiKey;
    private final String username;
    private final String nonce;
    private final byte[] digest;

    public AuthHeader(String algorithm, String apiKey, String username, String nonce, byte[] digest) {
        this.algorithm = algorithm;
        this.apiKey = apiKey;
        this.username = username;
        this.nonce = nonce;
        this.digest = digest;
    }


}
