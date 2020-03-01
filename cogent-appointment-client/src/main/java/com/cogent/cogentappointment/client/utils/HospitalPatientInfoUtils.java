package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;

/**
 * @author smriti on 01/03/20
 */
public class HospitalPatientInfoUtils {

    public static HospitalPatientInfo parseHospitalPatientInfo(Hospital hospital,
                                                               Patient patient,
                                                               Character isSelf,
                                                               String email,
                                                               String address) {

        HospitalPatientInfo hospitalPatientInfo = new HospitalPatientInfo();
        hospitalPatientInfo.setHospital(hospital);
        hospitalPatientInfo.setPatient(patient);
        hospitalPatientInfo.setIsSelf(isSelf);
        hospitalPatientInfo.setEmail(email);
        hospitalPatientInfo.setAddress(address);
        hospitalPatientInfo.setIsRegistered(NO);
        hospitalPatientInfo.setStatus(ACTIVE);
        return hospitalPatientInfo;
    }

}
