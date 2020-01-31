package com.cogent.cogentappointment.client.security.hmac;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.cogent.cogentappointment.client.constants.HMACConstant.*;
import static com.cogent.cogentappointment.client.utils.HMACKeyGenerator.generateNonce;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
public class HMACUtils {

    public String getAuthToken(HttpServletRequest request, Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .scheme(request.getScheme())
                .host(request.getServerName() + ":" + request.getServerPort())
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

    public String getAuthTokenForEsewa(HttpServletRequest request) {
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .scheme(request.getScheme())
                .host(request.getServerName() + ":" + request.getServerPort())
                .nonce(nonce)
                .apiKey(HMAC_API_KEY);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String authToken = HMAC_ALGORITHM +
                SPACE +
                HMAC_API_KEY +
                COLON +
                nonce +
                COLON +
                signature;

        return authToken;
    }

}
