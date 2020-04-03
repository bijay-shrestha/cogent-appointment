package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentTransactionStatusResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;

import java.util.Date;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.ACTIVE;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 31/03/20
 */
public class AppointmentTransactionRequestLogUtils {

    public static AppointmentTransactionRequestLog parseToAppointmentTransactionRequestLog
            (Date transactionDate,
             String transactionNumber,
             String patientName) {

        AppointmentTransactionRequestLog transactionRequestLog = new AppointmentTransactionRequestLog();
        transactionRequestLog.setTransactionDate(transactionDate);
        transactionRequestLog.setTransactionNumber(transactionNumber);
        transactionRequestLog.setPatientName(patientName);
        return transactionRequestLog;
    }

    public static void updateAppointmentTransactionRequestLog
            (AppointmentTransactionRequestLog appointmentTransactionRequestLog) {
        appointmentTransactionRequestLog.setTransactionStatus(ACTIVE);
    }

    public static AppointmentTransactionStatusResponseDTO parseToAppointmentTransactionStatusResponseDTO
            (Character transactionStatus) {

        return AppointmentTransactionStatusResponseDTO.builder()
                .transactionStatus(transactionStatus)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }
}
