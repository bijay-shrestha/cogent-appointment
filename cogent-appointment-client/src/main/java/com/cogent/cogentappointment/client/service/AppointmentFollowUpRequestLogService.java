package com.cogent.cogentappointment.client.service;

/**
 * @author smriti on 29/03/20
 */
public interface AppointmentFollowUpRequestLogService {

    Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId);
}
