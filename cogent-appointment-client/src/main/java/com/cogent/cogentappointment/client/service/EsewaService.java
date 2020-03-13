package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;

public interface EsewaService {

AppointmentDatesResponseDTO doctorAvailableTime(AppointmentDatesRequestDTO requestDTO);
}
