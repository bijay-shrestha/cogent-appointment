package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils.calculateAge;

/**
 * @author Sauravi Thapa ON 4/19/20
 */
public class TransactionLogUtils {

    public static TransactionLogResponseDTO parseQueryResultToTransactionLogResponse(List<Object[]> results) {

        TransactionLogResponseDTO appointmentLogResponseDTO = new TransactionLogResponseDTO();

        List<TransactionLogDTO> appointmentLogSearchDTOS = new ArrayList<>();

        results.forEach(result -> {
            final int HOSPITAL_NAME_INDEX = 0;
            final int APPOINTMENT_DATE_INDEX = 1;
            final int APPOINTMENT_NUMBER_INDEX = 2;
            final int APPOINTMENT_TIME_INDEX = 3;
            final int ESEWA_ID_INDEX = 4;
            final int REGISTRATION_NUMBER_INDEX = 5;
            final int PATIENT_NAME_INDEX = 6;
            final int PATIENT_GENDER_INDEX = 7;
            final int PATIENT_DOB_INDEX = 8;
            final int IS_REGISTERED_INDEX = 9;
            final int PATIENT_MOBILE_NUMBER_INDEX = 10;
            final int SPECIALIZATION_NAME_INDEX = 11;
            final int TRANSACTION_NUMBER_INDEX = 12;
            final int APPOINTMENT_AMOUNT_INDEX = 13;
            final int DOCTOR_NAME_INDEX = 14;
            final int APPOINTMENT_STATUS_INDEX = 15;
            final int REFUND_AMOUNT_INDEX = 16;
            final int TRANSACTION_DATE_INDEX = 17;
            final int APPOINTMENT_MODE_INDEX = 18;
            final int IS_FOLLOW_UP_INDEX = 19;
            final int PATIENT_ADDRESS_INDEX = 20;
            final int TRANSACTION_TIME_INDEX = 21;
            final int REVENUE_AMOUNT_INDEX = 22;

            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
            Date patientDob = (Date) result[PATIENT_DOB_INDEX];

            Double appointmentAmount = Objects.isNull(result[APPOINTMENT_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[APPOINTMENT_AMOUNT_INDEX].toString());

            Double refundAmount = Objects.isNull(result[REFUND_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[REFUND_AMOUNT_INDEX].toString());

            String registrationNumber = Objects.isNull(result[REGISTRATION_NUMBER_INDEX]) ?
                    null : result[REGISTRATION_NUMBER_INDEX].toString();


            TransactionLogDTO appointmentLogDTO =
                    TransactionLogDTO.builder()
                            .hospitalName(result[HOSPITAL_NAME_INDEX].toString())
                            .appointmentDate(appointmentDate)
                            .appointmentNumber(result[APPOINTMENT_NUMBER_INDEX].toString())
                            .appointmentTime(result[APPOINTMENT_TIME_INDEX].toString())
                            .esewaId(Objects.isNull(result[ESEWA_ID_INDEX]) ? null : result[ESEWA_ID_INDEX].toString())
                            .registrationNumber(registrationNumber)
                            .patientName(result[PATIENT_NAME_INDEX].toString())
                            .patientGender((Gender) result[PATIENT_GENDER_INDEX])
                            .patientDob(patientDob)
                            .patientAge(calculateAge(patientDob))
                            .isRegistered((Character) result[IS_REGISTERED_INDEX])
                            .mobileNumber(result[PATIENT_MOBILE_NUMBER_INDEX].toString())
                            .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
                            .transactionNumber(Objects.isNull(result[TRANSACTION_NUMBER_INDEX])
                                    ? null : result[TRANSACTION_NUMBER_INDEX].toString())
                            .appointmentAmount(appointmentAmount)
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .status(result[APPOINTMENT_STATUS_INDEX].toString())
                            .refundAmount(refundAmount)
                            .transactionDate((Date) result[TRANSACTION_DATE_INDEX])
                            .appointmentMode(result[APPOINTMENT_MODE_INDEX].toString())
                            .isFollowUp(result[IS_FOLLOW_UP_INDEX].toString().charAt(0))
                            .patientAddress(Objects.isNull(result[PATIENT_ADDRESS_INDEX])
                                    ? "" : result[PATIENT_ADDRESS_INDEX].toString())
                            .transactionTime(Objects.isNull(result[TRANSACTION_TIME_INDEX]) ? "" :
                                    result[TRANSACTION_TIME_INDEX].toString())
                            .revenueAmount(Double.parseDouble(result[REVENUE_AMOUNT_INDEX].toString()))
                            .build();

            appointmentLogSearchDTOS.add(appointmentLogDTO);

        });

        appointmentLogResponseDTO.setTransactionLogs(appointmentLogSearchDTOS);
        appointmentLogResponseDTO.setTotalItems(appointmentLogSearchDTOS.size());

        return appointmentLogResponseDTO;
    }
}
