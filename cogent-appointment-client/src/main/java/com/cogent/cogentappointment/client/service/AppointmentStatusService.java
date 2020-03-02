package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusDTO;

/**
 * @author smriti ON 16/12/2019
 */
public interface AppointmentStatusService {

    AppointmentStatusDTO fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO);
}
