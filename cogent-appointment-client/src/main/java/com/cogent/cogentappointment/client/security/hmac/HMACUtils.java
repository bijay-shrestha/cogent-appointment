package com.cogent.cogentappointment.client.security.hmac;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import org.springframework.stereotype.Component;

import static com.cogent.cogentappointment.client.constants.HMACConstant.*;
import static com.cogent.cogentappointment.client.utils.HMACKeyGenerator.generateNonce;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
public class HMACUtils {

    public String getHash(AdminMinDetails admin) {
        final String nonce = generateNonce();
        String username = admin.getUsername();
        String hospitalCode = admin.getHospitalCode();
        String apiKey = admin.getApiKey();
        String apiSecret = admin.getApiSecret();
        Integer hospitalId= Math.toIntExact(admin.getHospitalId());

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .nonce(nonce)
                .apiKey(apiKey)
                .hospitalCode(hospitalCode)
                .hospitalId(hospitalId)
                .username(username)
                .apiSecret(apiSecret);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String hash = HMAC_ALGORITHM +
                SPACE +
                username +
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

        final HMACBuilder signatureBuilder = new HMACBuilder()
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

}
