package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.persistence.model.Patient;

/**
 * @author smriti on 01/03/20
 */
public interface PatientRelationInfoService {

    void savePatientRelationInfo(Patient parentPatient, Patient childPatient);

}
