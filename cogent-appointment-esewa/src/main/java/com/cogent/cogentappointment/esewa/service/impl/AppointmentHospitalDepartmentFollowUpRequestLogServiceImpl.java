package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.AppointmentHospitalDepartmentFollowUpRequestLogRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentFollowUpRequestLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpRequestLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentHospitalDeptFollowUpRequestLog.APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_REQUEST_LOG;
import static com.cogent.cogentappointment.esewa.utils.AppointmentHospitalDepartmentFollowUpRequestLogUtils.updateAppointmentHospitalDeptFollowUpRequestLog;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 29/03/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentFollowUpRequestLogServiceImpl implements
        AppointmentHospitalDepartmentFollowUpRequestLogService {

    private final AppointmentHospitalDepartmentFollowUpRequestLogRepository appointmentHospitalDepartmentFollowUpRequestLogRepository;

    public AppointmentHospitalDepartmentFollowUpRequestLogServiceImpl(
            AppointmentHospitalDepartmentFollowUpRequestLogRepository appointmentHospitalDepartmentFollowUpRequestLogRepository) {
        this.appointmentHospitalDepartmentFollowUpRequestLogRepository = appointmentHospitalDepartmentFollowUpRequestLogRepository;
    }


    @Override
    public Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_REQUEST_LOG);

        Integer requestCount = appointmentHospitalDepartmentFollowUpRequestLogRepository.fetchRequestCountByFollowUpTrackerId
                (appointmentFollowUpTrackerId).orElseThrow(() ->
                APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentFollowUpTrackerId));

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_REQUEST_LOG,
                getDifferenceBetweenTwoTime(startTime));

        return requestCount;
    }

    @Override
    public void update(Long appointmentFollowUpTrackerId) {

        AppointmentHospitalDepartmentFollowUpRequestLog appointmentFollowUpRequestLog =
                appointmentHospitalDepartmentFollowUpRequestLogRepository.fetchByFollowUpTrackerId
                        (appointmentFollowUpTrackerId).orElseThrow(() ->
                        APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentFollowUpTrackerId));

        updateAppointmentHospitalDeptFollowUpRequestLog(appointmentFollowUpRequestLog);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND =
            (appointmentFollowUpTrackerId) -> {
                throw new NoContentFoundException(AppointmentHospitalDepartmentFollowUpRequestLog.class,
                        "appointmentFollowUpTrackerId", appointmentFollowUpTrackerId.toString());
            };
}
