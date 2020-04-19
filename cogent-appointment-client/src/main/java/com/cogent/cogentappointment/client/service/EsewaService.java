package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AvailableDoctorRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.AppointmentDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.*;

public interface EsewaService {

    AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO);

    DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO);

    AvailableDoctorWithSpecializationResponseDTO fetchAvailableDoctorWithSpecialization(AppointmentDetailRequestDTO requestDTO);

    AvailableDoctorWithSpecializationResponseDTO fetchAvailableDoctorWithSpecialization(AvailableDoctorRequestDTO requestDTO);

    AvailableDatesWithSpecializationResponseDTO fetchAvailableDatesWithSpecialization(Long doctorId);

    AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO);

    AvailableDatesWithDoctorResponseDTO fetchAvailableDatesWithDoctor(Long specializationId);

}
