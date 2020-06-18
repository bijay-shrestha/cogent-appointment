package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentTransactionStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.history.AppointmentResponseWithStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
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

    AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentHistorySearchDTO searchDTO);

    StatusResponseDTO cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO);

    StatusResponseDTO rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO);

    AppointmentDetailResponseWithStatusDTO fetchAppointmentDetails(Long appointmentId);

    AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentHistorySearchDTO searchDTO);

    AppointmentResponseWithStatusDTO searchAppointments(AppointmentSearchDTO searchDTO);

    StatusResponseDTO cancelRegistration(Long appointmentReservationId);

    AppointmentTransactionStatusResponseDTO fetchAppointmentTransactionStatus
            (AppointmentTransactionStatusRequestDTO requestDTO);

    /*admin*/
    /*PENDING APPROVAL*/
    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                                                      Pageable pageable);

    AppointmentPendingApprovalDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId);

    void approveAppointment(IntegrationBackendRequestDTO integrationBackendRequestDTO);

    void rejectAppointment(AppointmentRejectDTO rejectDTO);

    /*REFUND APPROVAL*/
    AppointmentRefundResponseDTO fetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO,
                                                                 Pageable pageable);

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long id);

    void approveRefundAppointment(IntegrationBackendRequestDTO integrationBackendRequestDTO);

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
