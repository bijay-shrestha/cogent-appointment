package com.cogent.cogentappointment.admin.security.hmac;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.cogent.cogentappointment.admin.constants.HMACConstant.*;
import static com.cogent.cogentappointment.admin.utils.HMACKeyGenerator.generateNonce;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
public class HMACUtils {

    public String getAuthToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .nonce(nonce)
                .apiKey(HMAC_API_KEY)
                .username(userPrincipal.getUsername());

        final String signature = signatureBuilder
                .buildAsBase64String();

        String authToken = HMAC_ALGORITHM +
                SPACE +
                userPrincipal.getUsername() +
                COLON +
                HMAC_API_KEY +
                COLON +
                nonce +
                COLON +
                signature;

        return authToken;
    }

}
