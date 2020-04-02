package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;

/**
 * @author smriti on 01/03/20
 */
public class HospitalPatientInfoUtils {

    public static HospitalPatientInfo parseHospitalPatientInfo(Hospital hospital,
                                                               Patient patient,
                                                               String email,
                                                               String address) {

        HospitalPatientInfo hospitalPatientInfo = new HospitalPatientInfo();
        hospitalPatientInfo.setHospital(hospital);
        hospitalPatientInfo.setPatient(patient);
        hospitalPatientInfo.setEmail(email);
        hospitalPatientInfo.setAddress(address);
        hospitalPatientInfo.setIsRegistered(NO);
        hospitalPatientInfo.setStatus(ACTIVE);
        return hospitalPatientInfo;
    }

    public static void updateHospitalPatientInfo(String email,
                                                 String address,
                                                 HospitalPatientInfo hospitalPatientInfo) {
        hospitalPatientInfo.setEmail(email);
        hospitalPatientInfo.setAddress(address);
    }

}
