package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.ACTIVE;

/**
 * @author smriti on 28/02/20
 */
public class PatientRelationInfoUtils {

    public static PatientRelationInfo parseToPatientRelationInfo(Patient parentPatientId,
                                                                 Patient childPatientId) {

        PatientRelationInfo patientRelationInfo = new PatientRelationInfo();
        patientRelationInfo.setParentPatientId(parentPatientId);
        patientRelationInfo.setChildPatientId(childPatientId);
        patientRelationInfo.setStatus(ACTIVE);
        return patientRelationInfo;
    }
}
