package com.cogent.cogentappointment.esewa.pki.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class AESEncryptionUtil {

    /**
     * method to encrypt using AES
     *
     * @param strToEncrypt - plaintext
     * @param secretKey
     * @return String - encrypted plaintext
     */
    static String encrypt(String strToEncrypt, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return base64Encode(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    /**
     * method to decrypt using AES
     *
     * @param strToDecrypt - encoded text
     * @param secretKey
     * @return String - plaintext
     */
    static String decrypt(String strToDecrypt, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(base64Decode(strToDecrypt)));
    }

    /**
     * Method to generate secret key
     *
     * @return SecretKey
     * @throws Exception
     */
    static SecretKey generateSecretKey() throws Exception {
        String secretStr = RandomGenerator.getAlphaNumericString(32);
        log.info("Generated secret key : " + secretStr);
        return getSecretKey(secretStr);
    }

    /**
     * @param secretKey
     * @return
     * @throws Exception
     */
    static SecretKey getSecretKey(String secretKey) throws Exception {
        byte[] decodeSecretKey = base64Decode(secretKey);
        log.info("length" + decodeSecretKey.length);
        return new SecretKeySpec(decodeSecretKey, 0, decodeSecretKey.length, "AES");
    }

    /**
     * @param data
     * @return String - encoded text
     */
    static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * @param data - encoded text
     * @return byte[]
     */
    static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * @param secretKey
     * @return String
     */
    static String keyToString(SecretKey secretKey) {
        byte encoded[] = secretKey.getEncoded();
        String encodedKey = base64Encode(encoded);
        return encodedKey;
    }
}
