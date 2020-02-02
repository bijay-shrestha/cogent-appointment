package com.cogent.cogentappointment.client.security.hmac;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.model.Admin;
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

    public String getAuthToken(AdminMinDetails admin) {
        final String nonce = generateNonce();
        String username=admin.getUsername();
        String hospitalCode=admin.getHospitalCode();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .nonce(nonce)
                .apiKey(HMAC_API_KEY)
                .hospitalCode(hospitalCode)
                .username(username);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String authToken = HMAC_ALGORITHM +
                SPACE +
                username +
                COLON+
                hospitalCode+
                COLON +
                HMAC_API_KEY +
                COLON +
                nonce +
                COLON +
                signature;

        return authToken;
    }

    public String getAuthTokenForEsewa() {
        final String nonce = generateNonce();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
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
