package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.*;

import java.util.List;

public interface EsewaService {

    AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    List<AvailableDoctorResponseDTO> fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);

    List<AvailableDateByDoctorIdResponseDTO> fetchAvailableDatesWithSpecialization(Long doctorId);

    AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO);

    List<AvailableDateBySpecializationIdResponseDTO> fetchAvailableDatesWithDoctor(Long specializationId);

}
