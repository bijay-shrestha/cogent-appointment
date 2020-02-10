package com.cogent.cogentappointment.client.security.hmac;

import lombok.Getter;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Getter
public class AuthHeader {

    private final String algorithm;

    private final String username;

    private final String companyCode;

    private final String apiKey;

    private final String nonce;

    private final byte[] digest;

    public AuthHeader(String algorithm, String username, String companyCode, String apiKey, String nonce, byte[] digest) {
        this.algorithm = algorithm;
        this.username = username;
        this.companyCode = companyCode;
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
    }
}
