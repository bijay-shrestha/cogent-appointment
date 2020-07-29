package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.constants.StringConstant.OR;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static void updatePatient(PatientUpdateRequestDTO requestDTO,
                                     Patient patient) {

        patient.setName(requestDTO.getName());
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

        if (Objects.isNull(hospitalPatientInfo.getRegistrationNumber()))
            patientMetaInfo.setMetaInfo(
                    updateRequestDTO.getName()
                            + OR + updateRequestDTO.getMobileNumber()
                            + OR + hospitalPatientInfo.getRegistrationNumber()
            );

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
            long l1 = Long.parseLong((latestRegistrationNumber.substring(alias.length() + 7)));

            String patientCount = (l1 < 9999) ?
                    String.format("%04d", l1 + 1) : String.format("%d", l1 + 1);

            registrationNumber = year + month + day + patientCount;
        }

        return Objects.isNull(alias) ? registrationNumber : alias + "-" + registrationNumber;
    }
}
