package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Qualifier("appointmentHospitalDepartmentFollowUpTrackerRepositoryCustom")
public interface AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom {

    AppointmentHospitalDepartmentFollowUpTracker fetchLatestAppointmentFollowUpTracker(Long parentAppointmentId);

    void updateAppointmentFollowUpTrackerStatus(Long hospitalId,
                                                Long hospitalDepartmentId,
                                                Long patientId);
}
