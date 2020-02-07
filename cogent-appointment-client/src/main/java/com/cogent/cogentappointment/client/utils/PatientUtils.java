package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static Patient parseToPatient(PatientRequestDTO requestDTO,
                                         Gender gender,
                                         Hospital hospital) {
        Patient patient = new Patient();
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setEmail(requestDTO.getEmail());
        patient.setIsSelf(requestDTO.getIsSelf());
        patient.setIsRegistered(NO);
        patient.setESewaId(requestDTO.getESewaId());
        patient.setAddress(requestDTO.getAddress());
        patient.setGender(gender);
        patient.setHospitalId(hospital);
        return patient;
    }

    public static Patient updatePatient(PatientUpdateRequestDTO requestDTO,
                                        Patient patient) {
        patient.setName(requestDTO.getName());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setAddress(requestDTO.getAddress());
        patient.setGender(requestDTO.getGender());
        patient.setHospitalNumber(requestDTO.getHospitalNumber());
        patient.setEmail(requestDTO.getEmail());
        patient.setRemarks(requestDTO.getRemarks());
        patient.setStatus(requestDTO.getStatus());

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
}
