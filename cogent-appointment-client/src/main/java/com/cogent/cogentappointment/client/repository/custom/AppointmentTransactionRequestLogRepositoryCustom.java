package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 31/03/20
 */
@Repository
@Qualifier("appointmentTransactionRequestLogRepositoryCustoms")
public interface AppointmentTransactionRequestLogRepositoryCustom {

    AppointmentTransactionRequestLog fetchAppointmentTransactionRequestLog(String transactionNumber,
                                                                           String patientName);
}
