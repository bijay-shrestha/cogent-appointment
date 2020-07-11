package com.cogent.cogentappointment.esewa.pki.utils;


import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.pki.PKIData;
import com.cogent.cogentappointment.esewa.pki.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Base64;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.PKIMessages.INVALID_TOKEN_SIGNATURE;

@Slf4j
public class SecurityUtil {

    public static PKIData encryptPayloadAndGenerateSignature(String payload,
                                                             String clientPublicKey,
                                                             String serverPrivateKey) {
        try {
            SecretKey secretKey = AESEncryptionUtil.generateSecretKey();
            String base64EncodedSecretKey = AESEncryptionUtil.keyToString(secretKey);

            byte[] encryptedSecretKey = RSAEncryptionUtil.encrypt(base64EncodedSecretKey, clientPublicKey);
            String finalSecretKey = AESEncryptionUtil.base64Encode(encryptedSecretKey);
            String encryptedData = AESEncryptionUtil.encrypt(payload, secretKey);

            String signature = generateSignature(encryptedData, serverPrivateKey);
            PKIData pkiData = new PKIData();
            pkiData.setData(encryptedData);
            pkiData.setSecretKey(finalSecretKey);
            pkiData.setSignature(signature);
            return pkiData;
        } catch (Exception e) {
            log.error("Error occurred while encrypting data error:{} stack:{}", e.getMessage(), e);
            throw new BadRequestException(INVALID_TOKEN_SIGNATURE, e.getMessage());
        }
    }

    public static String responseValidator(RequestWrapper requestWrapper, String clientPublicKey, String serverPrivateKey) {

        try {
            boolean verified = validateSignature(requestWrapper.getSignature(), requestWrapper.getData(), clientPublicKey);

            if (verified) {
                byte[] decodedSecretKey = AESEncryptionUtil.base64Decode(requestWrapper.getSecret_key());
                String plainSecretKey = RSAEncryptionUtil.decrypt(decodedSecretKey, serverPrivateKey);

                String data = AESEncryptionUtil.decrypt(requestWrapper.getData(),
                        AESEncryptionUtil.getSecretKey(plainSecretKey));

                log.info("DECRYPTED DATA :::::::::::::" + data);
                return data;
            } else {
                log.error("Error occurred while validating signature.");
                throw new BadRequestException(INVALID_TOKEN_SIGNATURE);
            }
        } catch (Exception e) {
            log.error("Error occurred while validating signature. :: {}", e.getMessage());
            throw new BadRequestException(INVALID_TOKEN_SIGNATURE, e.getMessage());
        }
    }

    private static boolean validateSignature(String receivedSignature, String payload, String clientPublicKey) {
        try {
            return RSAEncryptionUtil.verifySignature(payload, clientPublicKey, Base64.getDecoder().decode(receivedSignature));
        } catch (Exception e) {
            log.error("Error occurred while validating signature. :: {}", e.getMessage());
            throw new BadRequestException(INVALID_TOKEN_SIGNATURE, e.getMessage());
        }
    }

    private static String generateSignature(String payload, String privateKey) {
        try {
            String encodedSignature = Base64.getEncoder()
                    .encodeToString(RSAEncryptionUtil.generateSignature(payload.getBytes(), privateKey));

            log.info("Encoded Signature ::::::::::::" + encodedSignature);
            return encodedSignature;
        } catch (Exception e) {
            log.error("Exception : ", e);
            return null;
        }
    }
}
