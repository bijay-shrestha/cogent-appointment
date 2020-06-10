package com.cogent.cogentappointment.thirdparty.security.hmac;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.cogent.cogentappointment.thirdparty.constants.HMACConstant.DELIMITER;
import static com.cogent.cogentappointment.thirdparty.constants.HMACConstant.HMAC_ALGORITHM;


/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HMACBuilder {

    private String companyCode;

    private String nonce;

    private String algorithm;

    private String apiKey;

    private String apiSecret;


    public HMACBuilder companyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public HMACBuilder apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }


    public byte[] build() {
        try {
            final Mac digest = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(apiSecret.getBytes(), HMAC_ALGORITHM);
            digest.init(secretKey);
            digest.update(algorithm.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(companyCode.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(apiKey.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(nonce.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            final byte[] signatureBytes = digest.doFinal();
            digest.reset();
            return signatureBytes;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot create signature: " + e.getMessage(), e);
        }
    }

    public boolean isHashEquals(byte[] expectedSignature) {
        final byte[] signature = build();
        return MessageDigest.isEqual(signature, expectedSignature);
    }

    public String buildAsBase64String() { return DatatypeConverter.printBase64Binary(build()); }

}
