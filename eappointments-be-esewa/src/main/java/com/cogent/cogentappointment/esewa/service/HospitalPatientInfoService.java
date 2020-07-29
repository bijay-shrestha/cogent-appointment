package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestForDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;

/**
 * @author smriti on 29/02/20
 */
public interface HospitalPatientInfoService {

    void saveHospitalPatientInfoForSelf(Hospital hospital, Patient patient,
                                        PatientRequestByDTO patientRequestByDTO,
                                        Character hasAddress);

    void saveHospitalPatientInfoForOthers(Hospital hospital, Patient patient,
                                          PatientRequestForDTO patientRequestForDTO,
                                          Character hasAddress);

    Double fetchPatientAppointmentCharge(Long patientId,
                                         Long hospitalId,
                                         Long hospitalDepartmentId,
                                         Long hospitalDepartmentBillingModeId);
}
