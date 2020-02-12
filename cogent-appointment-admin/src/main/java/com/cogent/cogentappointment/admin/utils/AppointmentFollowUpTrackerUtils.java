package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.Specialization;

import java.util.Date;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author smriti on 18/11/2019
 */
public class AppointmentFollowUpTrackerUtils {

    public static AppointmentFollowUpTracker parseToAppointmentFollowUpTracker(
            String parentAppointmentNumber,
            Integer remainingFollowUpCount,
            Doctor doctor,
            Specialization specialization,
            Patient patient) {

        AppointmentFollowUpTracker followUpTracker = new AppointmentFollowUpTracker();
        followUpTracker.setDoctorId(doctor);
        followUpTracker.setPatientId(patient);
        followUpTracker.setSpecializationId(specialization);
        followUpTracker.setParentAppointmentNumber(parentAppointmentNumber);
        followUpTracker.setRemainingNumberOfFollowUps(remainingFollowUpCount);
        followUpTracker.setAppointmentApprovedDate(new Date());
        followUpTracker.setActive(ACTIVE);
        return followUpTracker;
    }
}
