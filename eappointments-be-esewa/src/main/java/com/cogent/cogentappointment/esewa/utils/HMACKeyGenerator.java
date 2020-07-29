package com.cogent.cogentappointment.esewa.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author Sauravi Thapa २०/१/२५
 */
public class HMACKeyGenerator {

    public static String generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }


}
