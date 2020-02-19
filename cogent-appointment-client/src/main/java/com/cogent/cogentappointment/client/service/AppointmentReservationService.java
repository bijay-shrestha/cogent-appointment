package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;

/**
 * @author smriti on 18/02/20
 */
public interface AppointmentReservationService {

    Long save(AppointmentFollowUpRequestDTO requestDTO);
}
