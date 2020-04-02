package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentCheckAvailabilityResponseDTO checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForSelf(AppointmentRequestDTOForSelf appointmentRequestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForOthers(AppointmentRequestDTOForOthers appointmentRequestDTO);

    List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO);

    void cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO);

    void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

    AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId);

    List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO);

    void cancelRegistration(Long appointmentReservationId);
}
