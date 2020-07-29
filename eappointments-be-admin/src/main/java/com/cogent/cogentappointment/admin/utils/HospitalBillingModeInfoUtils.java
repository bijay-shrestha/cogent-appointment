package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.BillingMode;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalBillingModeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.cogent.cogentappointment.admin.constants.StringConstant.D;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
public class HospitalBillingModeInfoUtils {

    public static BiFunction<Hospital, List<BillingMode>, List<HospitalBillingModeInfo>>
            parseToHospitalBillingModeInfoList = (hospital, billingModes) -> {

        List<HospitalBillingModeInfo> responseList = new ArrayList<>();
        billingModes.forEach(billingMode -> {
            HospitalBillingModeInfo hospitalBillingModeInfo = new HospitalBillingModeInfo();
            hospitalBillingModeInfo.setHospital(hospital);
            hospitalBillingModeInfo.setBillingMode(billingMode);
            hospitalBillingModeInfo.setStatus(hospital.getStatus());
            responseList.add(hospitalBillingModeInfo);
        });

        return responseList;
    };

    public static BiFunction<List<HospitalBillingModeInfo>, String, List<HospitalBillingModeInfo>>
            deleteHospitalBillingModeInfoList = (infoList, remarks) -> {

        infoList.forEach(info -> {
            info.setStatus(D);
            info.setRemarks(remarks);
        });

        return infoList;
    };
}
