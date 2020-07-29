package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;

/**
 * @author smriti on 29/03/20
 */
public interface AppointmentHospitalDepartmentFollowUpRequestLogService {

    void save(AppointmentHospitalDepartmentFollowUpTracker appointmentFollowUpTracker);

}
