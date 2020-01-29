package com.cogent.cogentappointment.client.security.hmac;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.CANNOT_CREATE_SIGNATURE;
import static com.cogent.cogentappointment.client.constants.HMACConstant.*;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HMACBuilder {

    private String scheme;
    private String host;
    private String username;
    private String apiKey;
    private String nonce;
    private String algorithm;

    public HMACBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public HMACBuilder username(String username) {
        this.username = username;
        return this;
    }

    public HMACBuilder algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public HMACBuilder host(String host) {
        this.host = host;
        return this;
    }

    public HMACBuilder nonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public HMACBuilder apiKey(String key) {
        this.apiKey = key;
        return this;
    }

    public byte[] build() {
        try {
            final Mac digest = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(HMAC_API_SECRET.getBytes(), HMAC_ALGORITHM);
            digest.init(secretKey);
            digest.update(algorithm.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(scheme.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(host.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(nonce.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update((username != null) ? username.getBytes(StandardCharsets.UTF_8) : null);
            digest.update(DELIMITER);
            digest.update(apiKey.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            final byte[] signatureBytes = digest.doFinal();
            digest.reset();
            return signatureBytes;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(CANNOT_CREATE_SIGNATURE + e.getMessage(), e);
        }
    }

    public boolean isHashEquals(byte[] expectedSignature) {
        final byte[] signature = build();
        return MessageDigest.isEqual(signature, expectedSignature);
    }

    public String buildAsBase64String() {
        return DatatypeConverter.printBase64Binary(build());
    }

}
