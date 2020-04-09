package com.cogent.cogentappointment.admin.security.hmac;

import com.cogent.cogentappointment.admin.service.impl.UserDetailsImpl;
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
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Integer adminId=Math.toIntExact(userPrincipal.getId());
        String username = userPrincipal.getUsername();
        String companyCode = userPrincipal.getCompanyCode();
        String apiKey = userPrincipal.getApiKey();
        String apiSecret = userPrincipal.getApiSecret();
        Integer companyId= Math.toIntExact(userPrincipal.getCompanyId());
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .id(adminId)
                .nonce(nonce)
                .apiKey(apiKey)
                .companyCode(companyCode)
                .companyId(companyId)
                .username(username)
                .apiSecret(apiSecret);;

        final String signature = signatureBuilder
                .buildAsBase64String();

        String authToken = HMAC_ALGORITHM +
                SPACE +
                adminId+
                COLON+
                username +
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


        return authToken;
    }

}
