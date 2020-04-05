package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentFollowUpRequestLogRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpRequestLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpRequestLogConstant.APPOINTMENT_FOLLOW_UP_REQUEST_LOG;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpRequestLogUtils.parseToAppointmentFollowUpRequestLog;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpRequestLogUtils.updateAppointmentFollowUpRequestLog;
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
    public void save(AppointmentFollowUpTracker appointmentFollowUpTracker) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG);

        AppointmentFollowUpRequestLog appointmentFollowUpRequestLog =
                parseToAppointmentFollowUpRequestLog(appointmentFollowUpTracker);

        save(appointmentFollowUpRequestLog);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG);

        Integer requestCount = appointmentFollowUpRequestLogRepository.fetchRequestCountByFollowUpTrackerId
                (appointmentFollowUpTrackerId)
                .orElseThrow(() ->
                        APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentFollowUpTrackerId));

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_REQUEST_LOG, getDifferenceBetweenTwoTime(startTime));

        return requestCount;
    }

    @Override
    public void update(Long appointmentFollowUpTrackerId) {
        AppointmentFollowUpRequestLog appointmentFollowUpRequestLog =
                appointmentFollowUpRequestLogRepository.fetchByFollowUpTrackerId
                        (appointmentFollowUpTrackerId)
                        .orElseThrow(() ->
                                APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentFollowUpTrackerId));

        updateAppointmentFollowUpRequestLog(appointmentFollowUpRequestLog);
    }

    private void save(AppointmentFollowUpRequestLog appointmentFollowUpRequestLog) {
        appointmentFollowUpRequestLogRepository.save(appointmentFollowUpRequestLog);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND =
            (appointmentFollowUpTrackerId) -> {
                throw new NoContentFoundException(AppointmentFollowUpRequestLog.class,
                        "appointmentFollowUpTrackerId", appointmentFollowUpTrackerId.toString());
            };


}
