package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.Patient;

import java.util.Date;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;

/**
 * @author smriti on 18/06/20
 */
public class AppointmentHospitalDepartmentFollowUpTrackerUtils {

    public static AppointmentHospitalDepartmentFollowUpTracker parseFollowUpDetails(Long parentAppointmentId,
                                                                                    Integer remainingFollowUpCount,
                                                                                    Hospital hospital,
                                                                                    HospitalDepartment hospitalDepartment,
                                                                                    Patient patient
    ) {

        AppointmentHospitalDepartmentFollowUpTracker followUpTracker = new AppointmentHospitalDepartmentFollowUpTracker();
        followUpTracker.setHospital(hospital);
        followUpTracker.setHospitalDepartment(hospitalDepartment);
        followUpTracker.setPatient(patient);
        followUpTracker.setParentAppointmentId(parentAppointmentId);
        followUpTracker.setRemainingNumberOfFollowUps(remainingFollowUpCount);
        followUpTracker.setAppointmentApprovedDate(new Date());
        followUpTracker.setStatus(ACTIVE);
        return followUpTracker;
    }

    public static void updateNumberOfAppointmentFollowUps(AppointmentHospitalDepartmentFollowUpTracker followUpTracker) {
        followUpTracker.setRemainingNumberOfFollowUps(followUpTracker.getRemainingNumberOfFollowUps() - 1);

        if (followUpTracker.getRemainingNumberOfFollowUps() <= 0)
            followUpTracker.setStatus(INACTIVE);
    }
}
