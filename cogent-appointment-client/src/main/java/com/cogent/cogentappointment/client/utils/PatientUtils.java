package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientMinimalResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StringConstant.OR;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static Patient parseToPatient(PatientRequestByDTO requestDTO,
                                         Gender gender) {
        Patient patient = new Patient();
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setESewaId(requestDTO.getESewaId());
        patient.setGender(gender);
        patient.setCogentId(generateRandomNumber(4));
        return patient;
    }

    public static Patient parseToPatient(String name,
                                         String mobileNumber,
                                         Date dateOfBirth,
                                         String eSewaId,
                                         Gender gender) {
        Patient patient = new Patient();
        patient.setName(toUpperCase(name));
        patient.setMobileNumber(mobileNumber);
        patient.setDateOfBirth(dateOfBirth);
        patient.setESewaId(eSewaId);
        patient.setGender(gender);
        patient.setCogentId(generateRandomNumber(4));
        return patient;
    }


    public static void updatePatient(PatientUpdateRequestDTO requestDTO,
                                     Patient patient) {
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setGender(requestDTO.getGender());
    }

    public static void updateHospitalPatientInfo(PatientUpdateRequestDTO requestDTO,
                                                 HospitalPatientInfo hospitalPatientInfo) {

        hospitalPatientInfo.setAddress(requestDTO.getAddress());
        hospitalPatientInfo.setHospitalNumber(requestDTO.getHospitalNumber());
        hospitalPatientInfo.setEmail(requestDTO.getEmail());
        hospitalPatientInfo.setRemarks(requestDTO.getRemarks());
        hospitalPatientInfo.setStatus(requestDTO.getStatus());
    }

    public static Function<Patient, PatientMinimalResponseDTO> parseToPatientMinimalResponseDTO =
            patient -> {

                return PatientMinimalResponseDTO.builder()
                        .patientId(patient.getId())
                        .name(patient.getName())
                        .mobileNumber(patient.getMobileNumber())
//                        .address(patient.)
//                .gender((Gender) object[GENDER_INDEX])
                        .build();
            };

    public static void updatePatientMetaInfo(HospitalPatientInfo hospitalPatientInfo,
                                             PatientMetaInfo patientMetaInfo,
                                             PatientUpdateRequestDTO updateRequestDTO) {
        patientMetaInfo.setMetaInfo(updateRequestDTO.getName()
                + OR +
                updateRequestDTO.getMobileNumber()
                + OR +
                hospitalPatientInfo.getRegistrationNumber());
        patientMetaInfo.setStatus(updateRequestDTO.getStatus());
        patientMetaInfo.setRemarks(updateRequestDTO.getRemarks());
    }

    public static void registerPatientDetails(HospitalPatientInfo hospitalPatientInfo,
                                              String latestRegistrationNumber) {
        hospitalPatientInfo.setIsRegistered(YES);
        hospitalPatientInfo.setRegistrationNumber(generateRegistrationNumber(latestRegistrationNumber));
    }

    /*REGISTRATION NUMBER IS GENERATED IN FORMAT :
    * FOR FIRST RECORD : YY + MM + DD + 0001
    *  eg.2002130001
    * THEN 0001 INCREMENTS BY 1
    *  NEXT REGISTRATION NUMBER = 2002130002
    *  NOTE THAT REGISTRATION NUMBER IS UNIQUELY GENERATED ONLY ONCE FOR THE PATIENT IN SPECIFIC HOSPITAL
    *  */
    private static String generateRegistrationNumber(String latestRegistrationNumber) {

        LocalDateTime date = LocalDateTime.now();

        int year = (date.getYear() % 100);

        String month = (date.getMonthValue() < 10) ? String.format("%02d", date.getMonthValue()) :
                String.format("%d", date.getMonthValue());

        String day = (date.getDayOfMonth() < 10) ? String.format("%02d", date.getDayOfMonth()) :
                String.format("%d", date.getDayOfMonth());

        String registrationNumber;

        if (Objects.isNull(latestRegistrationNumber)) {
            registrationNumber = year + month + day + String.format("%04d", 1);
        } else {
            long l1 = Long.parseLong((latestRegistrationNumber.substring(6)));

            String patientCount = (l1 < 9999) ?
                    String.format("%04d", l1 + 1) : String.format("%d", l1 + 1);

            registrationNumber = year + month + day + patientCount;
        }

        return registrationNumber;
    }
}
