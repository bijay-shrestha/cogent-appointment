package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;

/**
 * @author smriti on 29/02/20
 */
public interface HospitalPatientInfoService {

    void saveHospitalPatientInfo(Hospital hospital, Patient patient,
                                 Character isSelf, String email, String address);
}
