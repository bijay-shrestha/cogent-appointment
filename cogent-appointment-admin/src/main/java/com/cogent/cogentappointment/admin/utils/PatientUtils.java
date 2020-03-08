package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
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

    public static void updatePatientMetaInfo(PatientMetaInfo patientMetaInfo,
                                             String registrationNumber) {

        String metaInfo = patientMetaInfo.getMetaInfo();
        patientMetaInfo.setMetaInfo(metaInfo + OR + registrationNumber);
    }

}
