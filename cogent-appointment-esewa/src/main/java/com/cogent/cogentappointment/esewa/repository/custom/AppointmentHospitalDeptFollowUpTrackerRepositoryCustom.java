package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Qualifier("appointmentHospitalDeptFollowUpTrackerRepositoryCustom")
public interface AppointmentHospitalDeptFollowUpTrackerRepositoryCustom {

    AppointmentFollowUpTracker fetchAppointmentFollowUpTracker(Long patientId,
                                                               Long doctorId,
                                                               Long specializationId,
                                                               Long hospitalId);

}
