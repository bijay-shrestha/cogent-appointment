package com.cogent.cogentappointment.client.service;

/**
 * @author smriti on 29/03/20
 */
public interface AppointmentFollowUpRequestLogService {

    void save(Long appointmentFollowUpTrackerId);

    Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId);
}
