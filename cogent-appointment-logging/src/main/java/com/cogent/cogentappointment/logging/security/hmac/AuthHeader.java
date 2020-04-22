package com.cogent.cogentappointment.logging.security.hmac;

import lombok.Getter;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Getter
public class AuthHeader {

    private final String algorithm;

    private Integer id;

    private final String email;

    private final Integer hospitalId;

    private final String hospitalCode;

    private final String apiKey;

    private final String nonce;

    private final byte[] digest;

    public AuthHeader(String algorithm,
                      Integer id,
                      String email,
                      Integer hospitalId,
                      String hospitalCode,
                      String apiKey,
                      String nonce,
                      byte[] digest) {
        this.algorithm = algorithm;
        this.id=id;
        this.email = email;
        this.hospitalId=hospitalId;
        this.hospitalCode = hospitalCode;
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
    }
}
