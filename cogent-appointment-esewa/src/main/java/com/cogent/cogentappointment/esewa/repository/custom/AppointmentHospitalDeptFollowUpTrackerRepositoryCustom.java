package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Qualifier("appointmentHospitalDeptFollowUpTrackerRepositoryCustom")
public interface AppointmentHospitalDeptFollowUpTrackerRepositoryCustom {

    AppointmentHospitalDepartmentFollowUpTracker fetchAppointmentHospitalDeptFollowUpTracker(
            Long hospitalId, Long hospitalDepartmentId, Long patientId);
}
