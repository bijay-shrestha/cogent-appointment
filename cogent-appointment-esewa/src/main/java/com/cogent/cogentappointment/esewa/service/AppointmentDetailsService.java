package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.eSewa.*;

import java.util.List;

public interface AppointmentDetailsService {

    AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    List<AvailableDoctorResponseDTO> fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);

    List<AvailableDateByDoctorIdResponseDTO> fetchAvailableDatesWithSpecialization(Long doctorId);

    AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO);

    List<AvailableDateBySpecializationIdResponseDTO> fetchAvailableDatesWithDoctor(Long specializationId);

}
