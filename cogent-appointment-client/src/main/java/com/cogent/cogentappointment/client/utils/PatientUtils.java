package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.constants.StringConstant.OR;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static Patient parseToPatient(PatientRequestDTO requestDTO,
                                         Gender gender) {
        Patient patient = new Patient();
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setESewaId(requestDTO.getESewaId());
        patient.setGender(gender);

        return patient;
    }

    public static HospitalPatientInfo parseHospitalPatientInfo(PatientRequestDTO requestDTO,
                                                               Long patientId,
                                                               Long hospitalId) {
        HospitalPatientInfo hospitalPatientInfo = new HospitalPatientInfo();
        hospitalPatientInfo.setHospitalId(hospitalId);
        hospitalPatientInfo.setPatientId(patientId);
        hospitalPatientInfo.setIsSelf(requestDTO.getIsSelf());
        hospitalPatientInfo.setIsRegistered(NO);
        hospitalPatientInfo.setEmail(requestDTO.getEmail());
        hospitalPatientInfo.setAddress(requestDTO.getAddress());
        hospitalPatientInfo.setStatus(requestDTO.getStatus());
        return hospitalPatientInfo;
    }

    public static PatientMetaInfo parseToPatientMetaInfo(Patient patient,
                                                         String registrationNumber,
                                                         Character status) {
        PatientMetaInfo patientMetaInfo = new PatientMetaInfo();
        patientMetaInfo.setPatient(patient);
        patientMetaInfo.setMetaInfo(
                patient.getName() + OR + patient.getMobileNumber() + OR + registrationNumber);
        patientMetaInfo.setStatus(status);
        return patientMetaInfo;
    }


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

    public static List<PatientMinimalResponseDTO> parseToPatientMinimalResponseDTO(List<Object[]> results) {
        return results.stream()
                .map(parseToPatientMinimalResponseDTO)
                .collect(Collectors.toList());
    }

    public static Function<Object[], PatientMinimalResponseDTO> parseToPatientMinimalResponseDTO = object -> {

        final int PATIENT_ID_INDEX = 0;
        final int NAME_INDEX = 1;
        final int MOBILE_NUMBER_INDEX = 2;
        final int GENDER_INDEX = 3;

        //TODO: calculate age
        return PatientMinimalResponseDTO.builder()
                .patientId(Long.parseLong(object[PATIENT_ID_INDEX].toString()))
                .name(object[NAME_INDEX].toString())
                .mobileNumber(object[MOBILE_NUMBER_INDEX].toString())
                .gender((Gender) object[GENDER_INDEX])
                .build();
    };

    public static PatientMetaInfo updatePatientMetaInfo(Patient patient,
                                                        PatientMetaInfo patientMetaInfo,
                                                        PatientUpdateRequestDTO updateRequestDTO) {
        patientMetaInfo.setMetaInfo(updateRequestDTO.getName()
                        + OR +
                        updateRequestDTO.getMobileNumber()
                        + OR
//                patient.getRegistrationNumber());
        );
        patientMetaInfo.setStatus(updateRequestDTO.getStatus());
        patientMetaInfo.setRemarks(updateRequestDTO.getRemarks());

        return patientMetaInfo;
    }
}
