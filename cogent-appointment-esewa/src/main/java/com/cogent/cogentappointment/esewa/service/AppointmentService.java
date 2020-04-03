package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.*;

import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentCheckAvailabilityResponseDTO checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForSelf(AppointmentRequestDTOForSelf appointmentRequestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForOthers(AppointmentRequestDTOForOthers appointmentRequestDTO);

    AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentSearchDTO searchDTO);

    StatusResponseDTO cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO);

    StatusResponseDTO rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

    AppointmentDetailResponseWithStatusDTO fetchAppointmentDetails(Long appointmentId);

    AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentSearchDTO searchDTO);

    StatusResponseDTO cancelRegistration(Long appointmentReservationId);
}
