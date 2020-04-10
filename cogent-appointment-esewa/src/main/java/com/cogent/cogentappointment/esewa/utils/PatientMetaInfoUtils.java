package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.OR;

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

}
