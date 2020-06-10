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
import static com.cogent.cogentappointment.client.constants.HMACConstant.DELIMITER;
import static com.cogent.cogentappointment.client.constants.HMACConstant.HMAC_ALGORITHM;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HMACBuilderForEsewa {

    private String esewaId;

    private String merchantCode;

    private String algorithm;

    private String apiSecret;


    public HMACBuilderForEsewa esewaId(String esewaId) {
        this.esewaId = esewaId;
        return this;
    }


    public HMACBuilderForEsewa algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }


    public HMACBuilderForEsewa merchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
        return this;
    }

    public HMACBuilderForEsewa apiSecret(String apiSecret) {
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
            digest.update(esewaId.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);
            digest.update(merchantCode.getBytes(StandardCharsets.UTF_8));
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
