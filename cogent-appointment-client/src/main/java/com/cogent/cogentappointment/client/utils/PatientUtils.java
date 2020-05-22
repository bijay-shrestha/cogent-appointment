package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateDTOForOthers;
import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.patient.PatientDetailResponseDTOWithStatus;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTOForOthers;
import com.cogent.cogentappointment.client.dto.response.patient.PatientResponseDTOForOthersWithStatus;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.constants.StringConstant.OR;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;
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

    public static void updatePatient(PatientUpdateRequestDTO requestDTO,
                                     Patient patient) {
        patient.setName(toNormalCase(requestDTO.getName()));
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setGender(requestDTO.getGender());
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
        patientMetaInfo.setMetaInfo(toUpperCase(updateRequestDTO.getName())
                + OR +
                updateRequestDTO.getMobileNumber()
                + OR +
                hospitalPatientInfo.getRegistrationNumber());
        patientMetaInfo.setStatus(updateRequestDTO.getStatus());
        patientMetaInfo.setRemarks(updateRequestDTO.getRemarks());

        return patientMetaInfo;
    }

    public static void registerPatientDetails(HospitalPatientInfo hospitalPatientInfo,
                                              String latestRegistrationNumber) {
        hospitalPatientInfo.setIsRegistered(YES);
        hospitalPatientInfo.setRegistrationNumber(
                generateRegistrationNumber(latestRegistrationNumber, hospitalPatientInfo.getHospital().getAlias()));
    }

    /*REGISTRATION NUMBER IS GENERATED IN FORMAT :
     * FOR FIRST RECORD : YY + MM + DD + 0001
     *  eg.2002130001
     * THEN 0001 INCREMENTS BY 1
     *  NEXT REGISTRATION NUMBER = 2002130002
     *  NOTE THAT REGISTRATION NUMBER IS UNIQUELY GENERATED ONLY ONCE FOR THE PATIENT IN SPECIFIC HOSPITAL
     *  */
    private static String generateRegistrationNumber(String latestRegistrationNumber, String alias) {

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
            long l1 = Long.parseLong((latestRegistrationNumber.substring(alias.length()+7)));

            String patientCount = (l1 < 9999) ?
                    String.format("%04d", l1 + 1) : String.format("%d", l1 + 1);

            registrationNumber = year + month + day + patientCount;
        }

        return Objects.isNull(alias) ? registrationNumber : alias + "-" + registrationNumber;
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

    public static void parseHospitalWisePatientInfo(PatientDetailResponseDTO responseDTO,
                                                    Object[] result) {

        final int ADDRESS_INDEX = 0;
        final int EMAIL_INDEX = 1;
        final int REGISTRATION_NUMBER_INDEX = 2;

        responseDTO.setAddress(Objects.isNull(result[ADDRESS_INDEX]) ? null : result[ADDRESS_INDEX].toString());
        responseDTO.setEmail(Objects.isNull(result[EMAIL_INDEX]) ? null : result[EMAIL_INDEX].toString());
        responseDTO.setRegistrationNumber(Objects.isNull(result[REGISTRATION_NUMBER_INDEX])
                ? null : result[REGISTRATION_NUMBER_INDEX].toString());
    }
}
