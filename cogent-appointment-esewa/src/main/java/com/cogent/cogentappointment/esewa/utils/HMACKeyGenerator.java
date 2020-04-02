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

    public static String generateApiKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String generateApiSecret() throws
            NoSuchAlgorithmException {

        javax.crypto.KeyGenerator keyGen = javax.crypto.KeyGenerator.getInstance("DES");
        //Creating a SecureRandom object
        SecureRandom secRandom = new SecureRandom();
        //Initializing the KeyGenerator
        keyGen.init(secRandom);
        //Creating/Generating a key
        Key key = keyGen.generateKey();
        return DatatypeConverter.printHexBinary(key.getEncoded());
    }

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
