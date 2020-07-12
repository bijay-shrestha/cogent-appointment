package com.cogent.cogentappointment.admin.service.impl;


import com.cogent.cogentappointment.admin.repository.AppointmentHospitalDepartmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.admin.repository.HospitalRepository;
import com.cogent.cogentappointment.admin.service.AppointmentHospitalDepartmentFollowUpTrackerService;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_HOSPITAL_DEPARTMENT__FOLLOW_UP_TRACKER_STATUS;
import static com.cogent.cogentappointment.admin.utils.AppointmentHospitalDepartmentFollowUpTrackerUtils.parseFollowUpDetails;
import static com.cogent.cogentappointment.admin.utils.AppointmentHospitalDepartmentFollowUpTrackerUtils.updateNumberOfAppointmentFollowUps;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;


/**
 * @author smriti on 18/06/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentFollowUpTrackerServiceImpl implements
        AppointmentHospitalDepartmentFollowUpTrackerService {

    private final HospitalRepository hospitalRepository;

    private final AppointmentHospitalDepartmentFollowUpTrackerRepository appointmentHospitalDepartmentFollowUpTrackerRepository;

    public AppointmentHospitalDepartmentFollowUpTrackerServiceImpl(
            HospitalRepository hospitalRepository,
            AppointmentHospitalDepartmentFollowUpTrackerRepository appointmentHospitalDepartmentFollowUpTrackerRepository) {
        this.hospitalRepository = hospitalRepository;
        this.appointmentHospitalDepartmentFollowUpTrackerRepository = appointmentHospitalDepartmentFollowUpTrackerRepository;
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
    public AppointmentHospitalDepartmentFollowUpTracker save(Long parentAppointmentId,
                                                             Hospital hospital,
                                                             HospitalDepartment hospitalDepartment,
                                                             Patient patient) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER);

        appointmentHospitalDepartmentFollowUpTrackerRepository.updateAppointmentFollowUpTrackerStatus(
                hospital.getId(),
                hospitalDepartment.getId(),
                patient.getId()
        );

        Integer numberOfFollowUps = hospitalRepository.fetchHospitalFollowUpCount(hospital.getId());

        AppointmentHospitalDepartmentFollowUpTracker appointmentFollowUpTracker =
                save(parseFollowUpDetails(
                        parentAppointmentId, numberOfFollowUps, hospital, hospitalDepartment, patient)
                );

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER,
                getDifferenceBetweenTwoTime(startTime));

        return appointmentFollowUpTracker;
    }

    @Override
    public void updateFollowUpTracker(Long parentAppointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER);

        AppointmentHospitalDepartmentFollowUpTracker followUpTracker =
                appointmentHospitalDepartmentFollowUpTrackerRepository.fetchLatestAppointmentFollowUpTracker(
                        parentAppointmentId);

        updateNumberOfAppointmentFollowUps(followUpTracker);

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER,
                getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateFollowUpTrackerStatus() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_HOSPITAL_DEPARTMENT__FOLLOW_UP_TRACKER_STATUS);

        List<AppointmentHospitalDepartmentFollowUpTracker> followUpTrackers =
                appointmentHospitalDepartmentFollowUpTrackerRepository.fetchActiveFollowUpTracker();

        followUpTrackers.forEach(followUpTracker -> {
            int intervalDays = hospitalRepository.fetchHospitalFollowUpIntervalDays(
                    followUpTracker.getHospital().getId());

            Date expiryDate = utilDateToSqlDate(
                    addDays(followUpTracker.getAppointmentApprovedDate(), intervalDays));

            Date currentDate = utilDateToSqlDate(new Date());

            if ((Objects.requireNonNull(expiryDate).compareTo(Objects.requireNonNull(currentDate))) < 0)
                followUpTracker.setStatus(INACTIVE);
        });

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_HOSPITAL_DEPARTMENT__FOLLOW_UP_TRACKER_STATUS,
                getDifferenceBetweenTwoTime(startTime));
    }

    private AppointmentHospitalDepartmentFollowUpTracker save(AppointmentHospitalDepartmentFollowUpTracker followUpTracker) {
        return appointmentHospitalDepartmentFollowUpTrackerRepository.save(followUpTracker);
    }

}
