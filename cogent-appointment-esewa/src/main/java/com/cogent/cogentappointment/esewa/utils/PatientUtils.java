package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.request.patient.PatientUpdateDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientDetailResponseDTOWithStatus;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientResponseDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.response.patient.PatientResponseDTOForOthersWithStatus;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;

import java.util.Date;

import static com.cogent.cogentappointment.esewa.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.esewa.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.esewa.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.esewa.utils.commons.StringUtil.toUpperCase;
import static org.springframework.http.HttpStatus.OK;


/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static Patient parseToPatient(String name,
                                         String mobileNumber,
                                         Date dateOfBirth,
                                         String eSewaId,
                                         Gender gender) {
        Patient patient = new Patient();
        patient.setName(toNormalCase(name));
        patient.setMobileNumber(mobileNumber);
        patient.setDateOfBirth(dateOfBirth);
        patient.setESewaId(eSewaId);
        patient.setGender(gender);
        patient.setCogentId(generateRandomNumber(4));
        return patient;
    }

    public static void updateOtherPatient(PatientUpdateDTOForOthers requestDTO,
                                          HospitalPatientInfo hospitalPatientInfo) {

        hospitalPatientInfo.setEmail(requestDTO.getEmail());
        hospitalPatientInfo.setAddress(requestDTO.getAddress());

        Patient patient = hospitalPatientInfo.getPatient();
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setGender(fetchGenderByCode(requestDTO.getGender()));
    }

    public static PatientDetailResponseDTOWithStatus parseToPatientDetailResponseDTOWithStatus(
            PatientDetailResponseDTO responseDTO) {

        return PatientDetailResponseDTOWithStatus.builder()
                .detailResponseDTO(responseDTO)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static PatientResponseDTOForOthersWithStatus parseToPatientMinResponseDTOForOthersWithStatus(
            PatientResponseDTOForOthers responseDTO) {

        return PatientResponseDTOForOthersWithStatus.builder()
                .minResponseDTOForOthers(responseDTO)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }
}
