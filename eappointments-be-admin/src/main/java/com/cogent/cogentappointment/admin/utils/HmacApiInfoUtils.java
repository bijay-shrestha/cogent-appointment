package com.cogent.cogentappointment.admin.utils;


import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import com.cogent.cogentappointment.persistence.model.Hospital;

import java.security.NoSuchAlgorithmException;

import static com.cogent.cogentappointment.admin.utils.HMACKeyGenerator.generateApiKey;
import static com.cogent.cogentappointment.admin.utils.HMACKeyGenerator.generateApiSecret;

/**
 * @author Sauravi Thapa २०/२/२
 */
public class HmacApiInfoUtils {
    public static HmacApiInfo parseToHmacApiInfo(Hospital hospital) throws NoSuchAlgorithmException {
        HmacApiInfo hmacApiInfo = new HmacApiInfo();
        hmacApiInfo.setHospital(hospital);
        hmacApiInfo.setApiKey(generateApiKey());
        hmacApiInfo.setApiSecret(generateApiSecret());
        hmacApiInfo.setStatus(hospital.getStatus());

        return hmacApiInfo;
    }

    public static HmacApiInfo updateHmacApiInfoAsHospital(HmacApiInfo hmacApiInfo, Character status,String remarks) {
        hmacApiInfo.setStatus(status);
        hmacApiInfo.setRemarks(remarks);
        return hmacApiInfo;
    }
}
