package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTOWithStatus;
import com.cogent.cogentappointment.persistence.model.*;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentFollowUpTrackerService {

    /*esewa*/
    AppointmentFollowUpResponseDTOWithStatus fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO);

    AppointmentFollowUpTracker save(Long parentAppointmentId, Hospital hospital,
                                    Doctor doctor, Specialization specialization, Patient patient);

    void updateFollowUpTracker(Long parentAppointmentId);

    void updateFollowUpTrackerStatus();

    Long fetchByParentAppointmentId(Long parentAppointmentId);
}
