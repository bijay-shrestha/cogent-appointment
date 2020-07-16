package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER_STATUS;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpTracker;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpTrackerUtils.updateNumberOfFollowUps;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author smriti on 16/02/20
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

    /*
    * CASE FOR UPDATING APPOINTMENT FOLLOW UP TRACKER :
    *
    * TAKE 3 CONSECUTIVE APPOINTMENTS WITHOUT APPROVING
    * (THIS WILL PERSIST DATA IN APPOINTMENT TABLE AND OTHER RELATED TABLES)
    * THEN APPROVE/CHECK-IN EACH APPOINTMENTS SIMULTANEOUSLY.
    * FOR THE SAME PARAMETERS (HOSPITAL, DOCTOR, SPECIALIZATION, PATIENT),
    * IF FOLLOW UP TRACKER ALREADY EXISTS, THEN CHANGE OTHER STATUS AS 'N' AND REMAINING FOLLOW UP COUNT TO 0
    * AND PERSIST NEW FOLLOW UP REQUEST WITH NEW REMAINING NUMBER OF FOLLOWUPS.
    *
    * */
    @Override
    public AppointmentFollowUpTracker save(Long parentAppointmentId,
                                           Hospital hospital,
                                           Doctor doctor,
                                           Specialization specialization,
                                           Patient patient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        appointmentFollowUpTrackerRepository.updateAppointmentFollowUpTrackerStatus(
                patient.getId(),
                doctor.getId(),
                specialization.getId(),
                hospital.getId()
        );

        Integer numberOfFollowUps = hospitalRepository.fetchHospitalFollowUpCount(hospital.getId());

        AppointmentFollowUpTracker appointmentFollowUpTracker = save(parseToAppointmentFollowUpTracker(
                parentAppointmentId, numberOfFollowUps,
                doctor, specialization, patient, hospital)
        );

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return appointmentFollowUpTracker;
    }

    @Override
    public void updateFollowUpTracker(Long parentAppointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        AppointmentFollowUpTracker followUpTracker =
                appointmentFollowUpTrackerRepository.fetchLatestAppointmentFollowUpTracker(parentAppointmentId);

        updateNumberOfFollowUps(followUpTracker);

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    private AppointmentFollowUpTracker save(AppointmentFollowUpTracker followUpTracker) {
        return appointmentFollowUpTrackerRepository.save(followUpTracker);
    }

    @Override
    public void updateFollowUpTrackerStatus() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS);

        List<AppointmentFollowUpTracker> followUpTrackers =
                appointmentFollowUpTrackerRepository.fetchActiveFollowUpTracker();

        followUpTrackers.forEach(followUpTracker -> {
            int intervalDays = hospitalRepository.fetchHospitalFollowUpIntervalDays(
                    followUpTracker.getHospitalId().getId());

            Date expiryDate = utilDateToSqlDate(
                    addDays(followUpTracker.getAppointmentApprovedDate(), intervalDays));

            Date currentDate = utilDateToSqlDate(new Date());

            if ((Objects.requireNonNull(expiryDate).compareTo(Objects.requireNonNull(currentDate))) < 0)
                followUpTracker.setStatus(INACTIVE);
        });

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS, getDifferenceBetweenTwoTime(startTime));
    }
}
