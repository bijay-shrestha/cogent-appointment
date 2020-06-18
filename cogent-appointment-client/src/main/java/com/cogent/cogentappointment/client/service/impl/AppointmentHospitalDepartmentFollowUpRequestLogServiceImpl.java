package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentHospitalDepartmentFollowUpRequestLogRepository;
import com.cogent.cogentappointment.client.service.AppointmentHospitalDepartmentFollowUpRequestLogService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpRequestLog;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpRequestLogConstant.APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_REQUEST_LOG;
import static com.cogent.cogentappointment.client.utils.AppointmentHospitalDepartmentFollowUpRequestLogUtils.parseFollowUpRequestLogDetails;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
    public void save(AppointmentHospitalDepartmentFollowUpTracker appointmentFollowUpTracker) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_REQUEST_LOG);

        AppointmentHospitalDepartmentFollowUpRequestLog appointmentFollowUpRequestLog =
                parseFollowUpRequestLogDetails(appointmentFollowUpTracker);

        save(appointmentFollowUpRequestLog);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_REQUEST_LOG,
                getDifferenceBetweenTwoTime(startTime));
    }

    private void save(AppointmentHospitalDepartmentFollowUpRequestLog appointmentFollowUpRequestLog) {
        appointmentHospitalDepartmentFollowUpRequestLogRepository.save(appointmentFollowUpRequestLog);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_FOLLOW_REQUEST_LOG_WITH_GIVEN_ID_NOT_FOUND =
            (appointmentFollowUpTrackerId) -> {
                throw new NoContentFoundException(AppointmentFollowUpRequestLog.class,
                        "appointmentFollowUpTrackerId", appointmentFollowUpTrackerId.toString());
            };


}
