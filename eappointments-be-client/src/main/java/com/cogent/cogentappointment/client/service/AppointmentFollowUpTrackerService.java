package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.persistence.model.*;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentFollowUpTrackerService {

    AppointmentFollowUpTracker save(Long parentAppointmentId, Hospital hospital,
                                    Doctor doctor, Specialization specialization, Patient patient);

    void updateFollowUpTracker(Long parentAppointmentId);

    void updateFollowUpTrackerStatus();
}
