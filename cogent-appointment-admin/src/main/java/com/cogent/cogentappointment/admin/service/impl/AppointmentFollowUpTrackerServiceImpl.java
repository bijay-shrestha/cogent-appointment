package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.Specialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER_STATUS;
import static com.cogent.cogentappointment.admin.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpTracker;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 18/11/2019
 */
@Service
@Transactional
@Slf4j
public class AppointmentFollowUpTrackerServiceImpl implements AppointmentFollowUpTrackerService {

    private final AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository;

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
    }

//    @Override
//    public List<FollowUpTrackerResponseDTO> fetchMinimalFollowUpTracker(FollowUpTrackerSearchRequestDTO requestDTO) {
//
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(FETCHING_MINIMAL_PROCESS_STARTED, FOLLOW_UP_TRACKER);
//
//        List<FollowUpTrackerResponseDTO> responseDTOS =
//                followUpTrackerRepository.fetchMinimalFollowUpTracker(requestDTO);
//
//        log.info(FETCHING_MINIMAL_PROCESS_COMPLETED, FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
//
//        return responseDTOS;
//    }

    @Override
    public void save(String parentAppointmentNumber,
                     Doctor doctor,
                     Specialization specialization,
                     Patient patient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

//        Integer numberOfFollowUps = patientType.getCode().equalsIgnoreCase(IP_CODE) ?
//                followUpProperties.getIpNumberOfFollowups() : followUpProperties.getOpNumberOfFollowups();

        save(parseToAppointmentFollowUpTracker(
                parentAppointmentNumber, null, doctor, specialization, patient));

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

//    @Override
//    public void updateNumberOfFollowupsInFollowUpTracker(Long followUpTrackerId) {

//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER_COUNT);
//
//        AppointmentFollowUpTracker followUpTracker =
//                appointmentFollowUpTrackerRepository.fetchAppointmentFollowUpTracker(
//                parentAppointmentNumber, doctorId, patientId);
//
//        followUpTracker.setRemainingNumberOfFollowUps(followUpTracker.getRemainingNumberOfFollowUps() - 1);
//
//        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER_COUNT, getDifferenceBetweenTwoTime(startTime));
//    }

    private void save(AppointmentFollowUpTracker followUpTracker) {
        appointmentFollowUpTrackerRepository.save(followUpTracker);
    }

    @Override
    public void updateFollowUpTrackerStatus() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS);

//        List<AppointmentFollowUpTracker> followUpTrackers = appointmentFollowUpTrackerRepository.fetchActiveFollowUpTracker();

//        followUpTrackers.forEach(followUpTracker -> {
//            int intervalDays = (followUpTracker.getPatientTypeId().getCode().equalsIgnoreCase(IP_CODE)) ?
//                    (followUpProperties.getIpIntervalDays()) : (followUpProperties.getOpIntervalDays());
//
//            Date expiryDate = utilDateToSqlDate(
//                    addDays(followUpTracker.getAppointmentApprovedDate(), intervalDays));
//
//            Date currentDate = utilDateToSqlDate(new Date());
//
//            if ((Objects.requireNonNull(expiryDate).compareTo(Objects.requireNonNull(currentDate))) < 0)
//                followUpTracker.setActive(INACTIVE);
//        });

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS, getDifferenceBetweenTwoTime(startTime));
    }
}
