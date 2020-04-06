package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.repository.AppointmentFollowUpRequestLogRepository;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpRequestLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpRequestLogConstant.APPOINTMENT_FOLLOW_UP_REQUEST_LOG;
import static com.cogent.cogentappointment.admin.utils.AppointmentFollowUpRequestLogUtils.parseToAppointmentFollowUpRequestLog;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 29/03/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentFollowUpRequestLogServiceImpl implements AppointmentFollowUpRequestLogService {

    private final AppointmentFollowUpRequestLogRepository appointmentFollowUpRequestLogRepository;

    public AppointmentFollowUpRequestLogServiceImpl(
            AppointmentFollowUpRequestLogRepository appointmentFollowUpRequestLogRepository) {
        this.appointmentFollowUpRequestLogRepository = appointmentFollowUpRequestLogRepository;
    }

    @Override
    public void save(AppointmentFollowUpTracker appointmentFollowUpTracker) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG);

        AppointmentFollowUpRequestLog appointmentFollowUpRequestLog =
                parseToAppointmentFollowUpRequestLog(appointmentFollowUpTracker);

        save(appointmentFollowUpRequestLog);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG, getDifferenceBetweenTwoTime(startTime));
    }

    private void save(AppointmentFollowUpRequestLog appointmentFollowUpRequestLog) {
        appointmentFollowUpRequestLogRepository.save(appointmentFollowUpRequestLog);
    }
}
