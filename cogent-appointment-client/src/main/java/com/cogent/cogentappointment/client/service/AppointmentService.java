package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import org.springframework.data.domain.Pageable;

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

    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                                                      Pageable pageable);

    AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO, Pageable pageable);


    void approveAppointment(Long appointmentId);

    void rejectAppointment(AppointmentRejectDTO rejectDTO);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO, Pageable pageable);

    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable);

    void approveRefundAppointment(Long appointmentId);

    void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO);


}
