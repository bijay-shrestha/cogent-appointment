package com.cogent.cogentappointment.esewa.service;

/**
 * @author smriti on 29/03/20
 */
public interface AppointmentHospitalDepartmentFollowUpRequestLogService {

    Integer fetchRequestCountByFollowUpTrackerId(Long appointmentFollowUpTrackerId);

    void update(Long appointmentFollowUpTrackerId);
}
