package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;

public interface EsewaService {

    AppointmentDatesResponseDTO doctorAvailableTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    AvailableDoctorResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);


}
