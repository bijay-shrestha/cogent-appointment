package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentFollowUpRequestDTO;

/**
 * @author smriti on 18/02/20
 */
public interface AppointmentReservationService {

    Long save(AppointmentFollowUpRequestDTO requestDTO);
}
