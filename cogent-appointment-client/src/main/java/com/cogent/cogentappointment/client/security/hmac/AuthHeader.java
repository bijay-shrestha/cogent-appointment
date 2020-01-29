package com.cogent.cogentappointment.client.security.hmac;

/**
 * @author Sauravi Thapa २०/१/१९
 */
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

    public AuthHeader(String algorithm, String apiKey, String username, byte[] digest) {
        this(algorithm, apiKey, username, null, digest);
    }

    public String getAlgorithm() {

        return algorithm;
    }

    public String getApiKey() {
        return apiKey;
    }

    public byte[] getDigest() {

        return digest;
    }

    public String getNonce() {

        return nonce;
    }

    public String getUsername() {

        return username;
    }
}
