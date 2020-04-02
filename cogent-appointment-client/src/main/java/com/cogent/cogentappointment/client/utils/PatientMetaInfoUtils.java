package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StringConstant.OR;

/**
 * @author smriti on 01/03/20
 */
public class PatientMetaInfoUtils {

    public static PatientMetaInfo parseToPatientMetaInfo(Patient patient) {
        PatientMetaInfo patientMetaInfo = new PatientMetaInfo();
        patientMetaInfo.setPatient(patient);
        patientMetaInfo.setMetaInfo(
                patient.getName() + OR + patient.getMobileNumber());
        patientMetaInfo.setStatus(ACTIVE);
        return patientMetaInfo;
    }

    public static void updatePatientMetaInfoDetails(PatientMetaInfo patientMetaInfo,
                                                    String registrationNumber) {

        String metaInfo = patientMetaInfo.getMetaInfo();
        patientMetaInfo.setMetaInfo(metaInfo + OR + registrationNumber);
    }
}
