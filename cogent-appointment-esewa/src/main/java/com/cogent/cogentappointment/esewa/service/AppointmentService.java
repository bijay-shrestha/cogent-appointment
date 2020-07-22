package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.appointmentTxnStatus.AppointmentTransactionStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appointmentTxnStatus.AppointmentTransactionStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.cancel.AppointmentCancelResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentDetailResponseWithStatusDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentMinResponseWithStatusDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentResponseWithStatusDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.save.AppointmentSuccessResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentCheckAvailabilityResponseDTO fetchAvailableTimeSlots(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentCheckAvailabilityResponseDTO fetchCurrentAvailableTimeSlots(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForSelf(AppointmentRequestDTOForSelf appointmentRequestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForOthers(AppointmentRequestDTOForOthers appointmentRequestDTO);

    AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentHistorySearchDTO searchDTO);

    AppointmentCancelResponseDTO cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO);

    StatusResponseDTO rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

    AppointmentDetailResponseWithStatusDTO fetchAppointmentDetails(Long appointmentId);

    AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentHistorySearchDTO searchDTO);

    AppointmentResponseWithStatusDTO searchAppointments(AppointmentSearchDTO searchDTO,
                                                        Pageable pageable);

    StatusResponseDTO cancelRegistration(Long appointmentReservationId);

    AppointmentTransactionStatusResponseDTO fetchAppointmentTransactionStatus
            (AppointmentTransactionStatusRequestDTO requestDTO);
}
