package com.cogent.cogentappointment.admin.security.hmac;

import com.cogent.cogentappointment.admin.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

import static com.cogent.cogentappointment.admin.constants.HMACConstant.*;
import static com.cogent.cogentappointment.admin.security.hmac.HMACBuilderForEsewa.hmacShaGenerator;
import static com.cogent.cogentappointment.admin.utils.HMACKeyGenerator.generateNonce;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HMACUtils {

    public String getHash(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Integer id= Math.toIntExact(userPrincipal.getId());
        String email = userPrincipal.getEmail();
        String companyCode = userPrincipal.getCompanyCode();
        String apiKey = userPrincipal.getApiKey();
        String apiSecret = userPrincipal.getApiSecret();
        Integer companyId= Math.toIntExact(userPrincipal.getCompanyId());
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .id(id)
                .email(email)
                .companyCode(companyCode)
                .companyId(companyId)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .nonce(nonce);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String hash = HMAC_ALGORITHM +
                SPACE +
                id +
                COLON +
                email +
                COLON +
                companyId +
                COLON +
                companyCode +
                COLON +
                apiKey +
                COLON +
                nonce +
                COLON +
                signature;

        return hash;
    }

    public static BiFunction<String, String, String> getSigatureForEsewa = (esewaId, merchantCode) -> {

        final String signature = hmacShaGenerator(esewaId + COLON + merchantCode);

        return signature;
    };





}
