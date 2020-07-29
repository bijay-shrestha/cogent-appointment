package com.cogent.cogentappointment.client.security.hmac;

import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

import static com.cogent.cogentappointment.client.constants.HMACConstant.*;
import static com.cogent.cogentappointment.client.security.hmac.HMACBuilderForEsewa.hmacShaGenerator;
import static com.cogent.cogentappointment.client.utils.HMACKeyGenerator.generateNonce;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
public class HMACUtils {

    public String getHash(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Integer id = Math.toIntExact(userPrincipal.getId());
        String email = userPrincipal.getEmail();
        String hospitalCode = userPrincipal.getHospitalCode();
        String apiKey = userPrincipal.getApiKey();
        String apiSecret = userPrincipal.getApiSecret();
        Integer hospitalId = Math.toIntExact(userPrincipal.getHospitalId());
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .id(id)
                .nonce(nonce)
                .apiKey(apiKey)
                .hospitalCode(hospitalCode)
                .hospitalId(hospitalId)
                .email(email)
                .apiSecret(apiSecret);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String hash = HMAC_ALGORITHM +
                SPACE +
                id +
                COLON +
                email +
                COLON +
                hospitalId +
                COLON +
                hospitalCode +
                COLON +
                apiKey +
                COLON +
                nonce +
                COLON +
                signature;

        return hash;
    }

    public String getAuthTokenForEsewa(ThirdPartyDetail thirdPartyDetail) {
        final String nonce = generateNonce();
        String hospitalCode = thirdPartyDetail.getHospitalCode();
        String apiKey = thirdPartyDetail.getApiKey();
        String apiSecret = thirdPartyDetail.getApiSecret();

        final HMACBuilderEsewa signatureBuilder = new HMACBuilderEsewa()
                .algorithm(HMAC_ALGORITHM)
                .nonce(nonce)
                .apiSecret(apiSecret)
                .apiKey(apiKey)
                .hospitalCode(hospitalCode);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String authToken = HMAC_ALGORITHM +
                SPACE +
                hospitalCode +
                COLON +
                apiKey +
                COLON +
                nonce +
                COLON +
                signature;

        return authToken;
    }

    public static BiFunction<String, String, String> getSigatureForEsewa = (esewaId, merchantCode) -> {
        String messgae = esewaId + COLON + merchantCode;

        final String signature = hmacShaGenerator(messgae);

        return signature;
    };

}
