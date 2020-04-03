package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.repository.AppointmentTransactionRequestLogRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentTransactionRequestLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentTransactionRequestLogConstant.APPOINTMENT_TRANSACTION_REQUEST_LOG;
import static com.cogent.cogentappointment.esewa.utils.AppointmentTransactionRequestLogUtils.parseToAppointmentTransactionRequestLog;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 31/03/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentTransactionRequestLogServiceImpl implements AppointmentTransactionRequestLogService {

    private final AppointmentTransactionRequestLogRepository appointmentTransactionRequestLogRepository;

    public AppointmentTransactionRequestLogServiceImpl(
            AppointmentTransactionRequestLogRepository appointmentTransactionRequestLogRepository) {
        this.appointmentTransactionRequestLogRepository = appointmentTransactionRequestLogRepository;
    }

    @Override
    public AppointmentTransactionRequestLog save(Date transactionDate,
                                                 String transactionNumber,
                                                 String patientName) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_TRANSACTION_REQUEST_LOG);

        AppointmentTransactionRequestLog transactionRequestLog =
                fetchAppointmentTransactionRequestLog(transactionNumber, patientName);

        if (Objects.isNull(transactionRequestLog))
            transactionRequestLog = save(parseToAppointmentTransactionRequestLog
                    (transactionDate, transactionNumber, patientName));

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_TRANSACTION_REQUEST_LOG, getDifferenceBetweenTwoTime(startTime));

        return transactionRequestLog;
    }

    @Override
    public Character fetchAppointmentTransactionStatus(String transactionNumber,
                                                       String patientName) {

        return appointmentTransactionRequestLogRepository.fetchAppointmentTransactionStatus
                (transactionNumber, patientName);
    }

    private AppointmentTransactionRequestLog save(AppointmentTransactionRequestLog appointmentTransactionRequestLog) {
        return appointmentTransactionRequestLogRepository.save(appointmentTransactionRequestLog);
    }

    private AppointmentTransactionRequestLog fetchAppointmentTransactionRequestLog(String transactionNumber,
                                                                                   String patientName) {
        return appointmentTransactionRequestLogRepository.fetchAppointmentTransactionRequestLog
                (transactionNumber, patientName);
    }
}
