package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.Patient;

/**
 * @author smriti on 18/06/20
 */
public interface AppointmentHospitalDepartmentFollowUpTrackerService {

    AppointmentHospitalDepartmentFollowUpTracker save(Long parentAppointmentId, Hospital hospital,
                                                      HospitalDepartment hospitalDepartment, Patient patient);

    void updateFollowUpTracker(Long parentAppointmentId);

    void updateFollowUpTrackerStatus();
}
