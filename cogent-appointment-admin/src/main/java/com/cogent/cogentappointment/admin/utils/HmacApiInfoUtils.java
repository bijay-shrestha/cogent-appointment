package com.cogent.cogentappointment.admin.utils;


import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import com.cogent.cogentappointment.persistence.model.Hospital;

import java.security.NoSuchAlgorithmException;

/**
 * @author Sauravi Thapa २०/२/२
 */
public class HmacApiInfoUtils {
    public static HmacApiInfo parseToHmacApiInfo(Hospital hospital) throws NoSuchAlgorithmException {
        HmacApiInfo hmacApiInfo = new HmacApiInfo();
        hmacApiInfo.setHospital(hospital);
        hmacApiInfo.setApiKey(HMACKeyGenerator.generateApiKey());
        hmacApiInfo.setApiSecret(HMACKeyGenerator.generateApiSecret());
        hmacApiInfo.setStatus(hospital.getStatus());

        return hmacApiInfo;
    }
}
