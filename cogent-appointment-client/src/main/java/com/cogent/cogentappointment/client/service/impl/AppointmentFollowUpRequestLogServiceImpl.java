package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpRequestLog;
import com.cogent.cogentappointment.client.repository.AppointmentFollowUpRequestLogRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpRequestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpRequestLog.APPOINTMENT_FOLLOW_UP_REQUEST_LOG;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 29/03/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentFollowUpRequestLogServiceImpl implements AppointmentFollowUpRequestLogService {

    private final AppointmentFollowUpRequestLogRepository appointmentFollowUpRequestLogRepository;

    public AppointmentFollowUpRequestLogServiceImpl(AppointmentFollowUpRequestLogRepository appointmentFollowUpRequestLogRepository) {
        this.appointmentFollowUpRequestLogRepository = appointmentFollowUpRequestLogRepository;
    }

    @Override
    public void save(Long appointmentFollowUpTrackerId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG);


        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG);

        Integer requestCount =
                appointmentFollowUpRequestLogRepository.fetchRequestCountByFollowUpTrackerId(appointmentFollowUpTrackerId);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG, getDifferenceBetweenTwoTime(startTime));

        return requestCount;
    }

    private void save(com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog appointmentFollowUpRequestLog){
        appointmentFollowUpRequestLogRepository.save(appointmentFollowUpRequestLog);
    }
}
