package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentSuccessResponseDTO;

import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentCheckAvailabilityResponseDTO checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentSuccessResponseDTO save(AppointmentRequestDTO appointmentRequestDTO);

    List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO);

    void cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO);

    void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

    AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId);

    List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO);

    void cancelRegistration(Long appointmentReservationId);

}
