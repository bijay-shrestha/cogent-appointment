package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    /*esewa*/
    AppointmentCheckAvailabilityResponseDTO fetchAvailableTimeSlots(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentCheckAvailabilityResponseDTO fetchCurrentAvailableTimeSlots(AppointmentCheckAvailabilityRequestDTO requestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForSelf(AppointmentRequestDTOForSelf appointmentRequestDTO);

    AppointmentSuccessResponseDTO saveAppointmentForOthers(AppointmentRequestDTOForOthers appointmentRequestDTO);

    AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentSearchDTO searchDTO);

    StatusResponseDTO cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO);

    StatusResponseDTO rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

    AppointmentDetailResponseWithStatusDTO fetchAppointmentDetails(Long appointmentId);

    AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentSearchDTO searchDTO);

    StatusResponseDTO cancelRegistration(Long appointmentReservationId);

    AppointmentTransactionStatusResponseDTO fetchAppointmentTransactionStatus
            (AppointmentTransactionStatusRequestDTO requestDTO);

    /*admin*/
    /*PENDING APPROVAL*/
    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                                                      Pageable pageable);

    AppointmentPendingApprovalDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);

    void approveAppointment(Long appointmentId);

    void rejectAppointment(AppointmentRejectDTO rejectDTO);

    /*REFUND APPROVAL*/
    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable);

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long id);

    void approveRefundAppointment(Long appointmentId);

    void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO);

    /*APPOINTMENT STATUS*/
    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO,
                                                                            Long hospitalId);

    AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                    Pageable pageable);

    TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                    Pageable pageable);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                   Pageable pageable);

    /*APPOINTMENT QUEUE*/
    List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO searchRequestDTO, Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO, Pageable pageable);


}
