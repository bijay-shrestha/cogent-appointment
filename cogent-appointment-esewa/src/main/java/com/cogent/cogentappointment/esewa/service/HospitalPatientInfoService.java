package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;

/**
 * @author smriti on 29/02/20
 */
public interface HospitalPatientInfoService {

    void saveHospitalPatientInfoForSelf(Hospital hospital, Patient patient,
                                        String email, String address);

    void saveHospitalPatientInfoForOthers(Hospital hospital, Patient patient,
                                          String email, String address);
}
