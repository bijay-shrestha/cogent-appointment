package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.admin.repository.HospitalRepository;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER_STATUS;
import static com.cogent.cogentappointment.admin.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpTracker;
import static com.cogent.cogentappointment.admin.utils.AppointmentFollowUpTrackerUtils.updateNumberOfFreeFollowUps;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author smriti on 18/11/2019
 */
@Service
@Transactional
@Slf4j
public class AppointmentFollowUpTrackerServiceImpl implements AppointmentFollowUpTrackerService {

    private final AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository;

    private final HospitalRepository hospitalRepository;

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository,
                                                 HospitalRepository hospitalRepository) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void save(Long parentAppointmentId,
                     String parentAppointmentNumber,
                     Hospital hospital,
                     Doctor doctor,
                     Specialization specialization,
                     Patient patient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        Integer numberOfFreeFollowUps = hospitalRepository.fetchHospitalFollowUpCount(hospital.getId());

        save(parseToAppointmentFollowUpTracker(
                parentAppointmentId, parentAppointmentNumber, numberOfFreeFollowUps,
                doctor, specialization, patient, hospital));

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateFollowUpTracker(Long parentAppointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        AppointmentFollowUpTracker followUpTracker =
                appointmentFollowUpTrackerRepository.fetchLatestAppointmentFollowUpTracker(parentAppointmentId);

        updateNumberOfFreeFollowUps(followUpTracker);

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    private void save(AppointmentFollowUpTracker followUpTracker) {
        appointmentFollowUpTrackerRepository.save(followUpTracker);
    }

    @Override
    public void updateFollowUpTrackerStatus() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS);

        List<AppointmentFollowUpTracker> followUpTrackers =
                appointmentFollowUpTrackerRepository.fetchActiveFollowUpTracker();

        followUpTrackers.forEach(followUpTracker -> {
            int intervalDays = hospitalRepository.fetchHospitalFreeFollowUpIntervalDays(
                    followUpTracker.getHospitalId().getId());

            Date expiryDate = utilDateToSqlDate(
                    addDays(followUpTracker.getAppointmentApprovedDate(), intervalDays));

            Date currentDate = utilDateToSqlDate(new Date());

            if ((Objects.requireNonNull(expiryDate).compareTo(Objects.requireNonNull(currentDate))) < 0){

                followUpTracker.setStatus(INACTIVE);
                appointmentFollowUpTrackerRepository.save(followUpTracker);
            }
        });

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS, getDifferenceBetweenTwoTime(startTime));
    }
}
