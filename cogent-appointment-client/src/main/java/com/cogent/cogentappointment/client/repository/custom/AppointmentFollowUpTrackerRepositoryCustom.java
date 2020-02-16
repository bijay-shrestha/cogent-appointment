package com.cogent.cogentappointment.client.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Qualifier("appointmentFollowUpTrackerRepositoryCustom")
public interface AppointmentFollowUpTrackerRepositoryCustom {

    List<Object[]> fetchFollowUpDetails(Long patientId,
                                        Long doctorId,
                                        Long specializationId,
                                        Long hospitalId);
}
