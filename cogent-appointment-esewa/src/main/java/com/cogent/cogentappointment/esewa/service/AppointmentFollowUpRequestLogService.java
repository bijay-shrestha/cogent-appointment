package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;

/**
 * @author smriti on 29/03/20
 */
public interface AppointmentFollowUpRequestLogService {

    Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId);

    void update(Long appointmentFollowUpTrackerId);
}
