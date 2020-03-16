package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.AllAvaliableDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.AvaliableDateByDoctorIdResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;

import java.util.List;

public interface EsewaService {

    AppointmentDatesResponseDTO fetchDoctorAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    List<AvaliableDateByDoctorIdResponseDTO> fetchDoctorAvailableDatesWithSpecialization(Long doctorId);

    AvailableDoctorResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);

    AllAvaliableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO);


}
