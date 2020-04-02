package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.eSewa.*;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;


import java.util.List;

public interface AppointmentDetailsService {

    AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    AvailableDoctorWithSpecializationResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);

    AvailableDatesWithSpecializationResponseDTO fetchAvailableDatesWithSpecialization(Long doctorId);

    AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO);

    AvailableDatesWithDoctorResponseDTO fetchAvailableDatesWithDoctor(Long specializationId);

}
