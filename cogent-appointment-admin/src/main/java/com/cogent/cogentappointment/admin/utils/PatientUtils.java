package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import static com.cogent.cogentappointment.admin.constants.StringConstant.OR;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {
    public static Patient updatePatient(PatientUpdateRequestDTO requestDTO,
                                        Patient patient) {
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setMobileNumber(requestDTO.getMobileNumber());
//        patient.setAddress(requestDTO.getAddress());
        patient.setGender(requestDTO.getGender());
//        patient.setHospitalNumber(requestDTO.getHospitalNumber());
//        patient.setEmail(requestDTO.getEmail());
//        patient.setRemarks(requestDTO.getRemarks());
//        patient.setStatus(requestDTO.getStatus());

        return patient;
    }

    public static PatientMetaInfo updatePatientMetaInfo(Patient patient,
                                                        PatientMetaInfo patientMetaInfo,
                                                        PatientUpdateRequestDTO updateRequestDTO) {
//merge with
        patientMetaInfo.setStatus(updateRequestDTO.getStatus());
        patientMetaInfo.setRemarks(updateRequestDTO.getRemarks());

        return patientMetaInfo;
    }

}
