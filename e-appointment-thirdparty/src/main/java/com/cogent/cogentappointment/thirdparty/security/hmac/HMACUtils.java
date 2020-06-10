package com.cogent.cogentappointment.thirdparty.security.hmac;

import com.cogent.cogentappointment.thirdparty.dto.request.login.ThirdPartyDetail;
import org.springframework.stereotype.Component;

import static com.cogent.cogentappointment.thirdparty.constants.HMACConstant.COLON;
import static com.cogent.cogentappointment.thirdparty.constants.HMACConstant.HMAC_ALGORITHM;
import static com.cogent.cogentappointment.thirdparty.constants.HMACConstant.SPACE;
import static com.cogent.cogentappointment.thirdparty.utils.HMACKeyGenerator.generateNonce;


/**
 * @author Sauravi Thapa २०/१/१९
 */

@Component
public class HMACUtils {

    public static String getAuthToken(ThirdPartyDetail thirdPartyDetail) {
        final String nonce = generateNonce();
        String companyCode = thirdPartyDetail.getCompanyCode();
        String apiKey = thirdPartyDetail.getApiKey();
        String apiSecret = thirdPartyDetail.getApiSecret();

        final HMACBuilder signatureBuilder = new HMACBuilder()
                .algorithm(HMAC_ALGORITHM)
                .nonce(nonce)
                .apiSecret(apiSecret)
                .apiKey(apiKey)
                .companyCode(companyCode);

        final String signature = signatureBuilder
                .buildAsBase64String();

        String authToken = HMAC_ALGORITHM +
                SPACE +
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
