package com.cogent.cogentappointment.admin.security.hmac;

import lombok.Getter;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Getter
public class AuthHeader {

    private final String algorithm;

    private final String username;

    private final Integer companyId;

    private final String companyCode;

    private final String apiKey;

    private final String nonce;

    private final byte[] digest;

    public AuthHeader(String algorithm,
                      String username,
                      Integer companyId,
                      String companyCode,
                      String apiKey,
                      String nonce,
                      byte[] digest) {
        this.algorithm = algorithm;
        this.username = username;
        this.companyId = companyId;
        this.companyCode = companyCode;
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
    }
}
