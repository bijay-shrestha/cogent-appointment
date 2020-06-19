package com.cogent.cogentappointment.client.security.hmac;

import com.cogent.cogentappointment.client.exception.BadRequestException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import static com.cogent.cogentappointment.client.constants.HMACConstant.HMAC_ALGORITHM;
import static com.cogent.cogentappointment.client.constants.HMACConstant.HMAC_API_SECRET_ESEWA;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HMACBuilderForEsewa {

    public static String hmacShaGenerator(String message) {
        try {
            Key secretKeySpec = new SecretKeySpec(HMAC_API_SECRET_ESEWA.getBytes(), HMAC_ALGORITHM);
            Mac msgAuthenticationCode = Mac.getInstance(secretKeySpec.getAlgorithm());
            msgAuthenticationCode.init(secretKeySpec);
            final byte[] hmac = msgAuthenticationCode.doFinal(message.getBytes());
            StringBuilder stringBuilder = new StringBuilder(hmac.length * 2);
            try (Formatter formatter = new Formatter(stringBuilder)) {
                for (byte ba : hmac) {
                    formatter.format("%02x", ba);
                }
            }
            String hashMacAuthString = stringBuilder.toString();
            return hashMacAuthString;
        } catch (NoSuchAlgorithmException noSuchAlgEx) {
            throw new BadRequestException("Error building the signature. No such authorization key. Error message : "
                    + noSuchAlgEx.getMessage());
        } catch (InvalidKeyException invalidKeyEx) {
            throw new BadRequestException("Error building the signature. Invalid key. Error Message : "
                    + invalidKeyEx.getMessage());
        }
    }
}
