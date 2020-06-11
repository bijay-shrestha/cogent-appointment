package com.cogent.cogentappointment.thirdparty.utils;

import java.security.SecureRandom;

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
