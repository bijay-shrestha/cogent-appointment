package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Qualifier("followUpRepositoryCustom")
public interface AppointmentFollowUpTrackerRepositoryCustom {


//    List<FollowUpTrackerResponseDTO> fetchMinimalFollowUpTracker(FollowUpTrackerSearchRequestDTO requestDTO);

    AppointmentFollowUpTracker fetchAppointmentFollowUpTracker(String parentAppointmentNumber,
                                                               Long doctorId,
                                                               Long patientId,
                                                               Long specializationId);

    AppointmentFollowUpTracker fetchLatestAppointmentFollowUpTracker(Long parentAppointmentId);

}
