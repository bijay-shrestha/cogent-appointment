package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;

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
        patientRelationInfo.setIsOther(YES);
        return patientRelationInfo;
    }
}
