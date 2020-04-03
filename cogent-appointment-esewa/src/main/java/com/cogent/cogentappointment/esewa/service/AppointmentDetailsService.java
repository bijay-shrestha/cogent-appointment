package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.*;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;

public interface AppointmentDetailsService {

    AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    AvailableDoctorWithSpecializationResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);

    AvailableDatesWithSpecializationResponseDTO fetchAvailableDatesWithSpecialization(Long doctorId);

    AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO);

    AvailableDatesWithDoctorResponseDTO fetchAvailableDatesWithDoctor(Long specializationId);

}
