package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
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
        patient.setGender(requestDTO.getGender());


        return patient;
    }

    public static HospitalPatientInfo updateHospitalPatientInfo(PatientUpdateRequestDTO requestDTO,
                                                                HospitalPatientInfo hospitalPatientInfo) {

        hospitalPatientInfo.setAddress(requestDTO.getAddress());
        hospitalPatientInfo.setHospitalNumber(requestDTO.getHospitalNumber());
        hospitalPatientInfo.setEmail(requestDTO.getEmail());
        hospitalPatientInfo.setRemarks(requestDTO.getRemarks());
        hospitalPatientInfo.setStatus(requestDTO.getStatus());

        return hospitalPatientInfo;
    }

    public static PatientMetaInfo updatePatientMetaInfo(HospitalPatientInfo hospitalPatientInfo,
                                                        PatientMetaInfo patientMetaInfo,
                                                        PatientUpdateRequestDTO updateRequestDTO) {
        patientMetaInfo.setMetaInfo(updateRequestDTO.getName()
                + OR +
                updateRequestDTO.getMobileNumber()
                + OR+
                hospitalPatientInfo.getRegistrationNumber());
        patientMetaInfo.setStatus(updateRequestDTO.getStatus());
        patientMetaInfo.setRemarks(updateRequestDTO.getRemarks());

        return patientMetaInfo;
    }

}
