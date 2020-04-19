package com.cogent.cogentappointment.admin.security.hmac;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.CANNOT_CREATE_SIGNATURE;
import static com.cogent.cogentappointment.admin.constants.HMACConstant.*;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HMACBuilder {

    private Integer id;

    private String username;

    private String companyCode;

    private Integer companyId;

     private String nonce;

    private String algorithm;

    private String apiKey;

    private String apiSecret;

    public HMACBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public HMACBuilder username(String username) {
        this.username = username;
        return this;
    }

    public HMACBuilder companyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public HMACBuilder companyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public HMACBuilder apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public HMACBuilder algorithm(String algorithm) {
        this.algorithm = algorithm;
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
            SecretKeySpec secretKey = new SecretKeySpec(apiSecret.getBytes(), HMAC_ALGORITHM);
            digest.init(secretKey);
            digest.update(algorithm.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(nonce.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update((username != null) ? username.getBytes(StandardCharsets.UTF_8) : null);
            digest.update(DELIMITER);
            digest.update(ByteBuffer.allocateDirect(companyId));
            digest.update(DELIMITER);
            digest.update(companyCode.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(apiKey.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(ByteBuffer.allocateDirect(id));
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
