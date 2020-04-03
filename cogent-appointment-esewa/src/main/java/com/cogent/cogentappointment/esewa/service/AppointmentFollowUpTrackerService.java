package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentFollowUpResponseDTOWithStatus;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentFollowUpTrackerService {

    /*esewa*/
    AppointmentFollowUpResponseDTOWithStatus fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO);
}
