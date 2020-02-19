package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueSearchByTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author smriti on 2019-10-22
 */
public interface AppointmentService {

    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable);

    void approveRefundAppointment(Long appointmentId);

    void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO);

    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);

    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                                                      Pageable pageable);

    void approveAppointment(Long appointmentId);

    void rejectAppointment(AppointmentRejectDTO rejectDTO);

    AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO, Pageable pageable);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO, Pageable pageable);

    AppointmentQueueSearchDTO fetchTodayAppointmentQueue(AppointmentQueueRequestDTO searchRequestDTO, Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(AppointmentQueueRequestDTO appointmentQueueRequestDTO, Pageable pageable);
}
