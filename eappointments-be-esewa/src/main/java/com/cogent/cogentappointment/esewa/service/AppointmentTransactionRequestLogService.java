package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;

import java.util.Date;

/**
 * @author smriti on 31/03/20
 */
public interface AppointmentTransactionRequestLogService {

    AppointmentTransactionRequestLog save(Date transactionDate,
                                          String transactionNumber,
                                          String patientName);

    Character fetchAppointmentTransactionStatus(String transactionNumber, String patientName);
}
