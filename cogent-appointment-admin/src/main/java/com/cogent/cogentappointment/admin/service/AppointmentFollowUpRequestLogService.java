package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;

/**
 * @author smriti on 29/03/20
 */
public interface AppointmentFollowUpRequestLogService {

    void save(AppointmentFollowUpTracker appointmentFollowUpTracker);
}
