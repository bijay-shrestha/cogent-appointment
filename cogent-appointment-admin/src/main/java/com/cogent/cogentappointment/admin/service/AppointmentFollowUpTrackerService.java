package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.persistence.model.*;

/**
 * @author smriti on 18/11/2019
 */
public interface AppointmentFollowUpTrackerService {

    AppointmentFollowUpTracker save(Long parentAppointmentId, Hospital hospital,
                                    Doctor doctor, Specialization specialization, Patient patient);

    void updateFollowUpTracker(Long parentAppointmentId);

    void updateFollowUpTrackerStatus();
}
