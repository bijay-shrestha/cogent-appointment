package com.cogent.cogentappointment.esewa.pki.utils;


import com.cogent.cogentappointment.esewa.pki.PKIData;
import com.cogent.cogentappointment.esewa.pki.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
public class SecurityUtil {

    protected static PKIData encryptPayloadAndGenerateSignature(String payload,
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
        } catch (Exception exe) {
            log.error("Error occurred while encrypting data error:{} stack:{}", exe.getMessage(), exe);

            //todo: exception
            return null;
//            throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(),
//                    ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
        }
    }

    protected static String responseValidator(String payload, String clientPublicKey, String serverPrivateKey) {
        try {
            RequestWrapper requestWrapper = JacksonUtil.get(payload, RequestWrapper.class);
            boolean verified = validateSignature(requestWrapper.getSignature(), requestWrapper.getData(), clientPublicKey);

            byte[] decodedSecretKey = AESEncryptionUtil.base64Decode(requestWrapper.getSecret_key());
            String plainSecretKey = RSAEncryptionUtil.decrypt(decodedSecretKey, serverPrivateKey);

            String data = AESEncryptionUtil.decrypt(requestWrapper.getData(),
                    AESEncryptionUtil.getSecretKey(plainSecretKey));

            if (verified) {
                return data;
            } else {
                log.error("Error occurred while validating signature.");

                return null;

                //todo: exception
//                throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(), ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
            }
        } catch (Exception e) {
            log.error("Error occurred while validating signature. :: {}", e.getMessage());

            return null;

            //todo: exception
//
//            throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(), ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
        }
    }

    private static boolean validateSignature(String receivedSignature, String payload, String clientPublicKey) {
        try {
            return RSAEncryptionUtil.verifySignature(payload, clientPublicKey, Base64.getDecoder().decode(receivedSignature));
        } catch (Exception e) {
            log.error("Error occurred while validating signature. :: {}", e.getMessage());

            return true;

            //todo: exception
//            throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(), ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
        }
    }

    private static String generateSignature(String payload, String privateKey) {
        try {
            String encodedSignature = Base64.getEncoder().encodeToString(RSAEncryptionUtil.generateSignature(payload.getBytes(), privateKey));
            return encodedSignature;
        } catch (Exception e) {
            log.error("Exception : ", e);
            return null;
        }
    }
}
