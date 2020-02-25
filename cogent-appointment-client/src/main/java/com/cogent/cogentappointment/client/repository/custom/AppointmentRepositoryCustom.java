package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRepositoryCustom")
public interface AppointmentRepositoryCustom {

    /*eSewa*/
    Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                     Long doctorId, Long specializationId);

    String generateAppointmentNumber(String nepaliCreatedDate, Long hospitalId);

    List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO);

    Double calculateRefundAmount(Long appointmentId);

    AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId);

    List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO);

    /*admin*/
    List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(AppointmentCheckAvailabilityRequestDTO requestDTO);

    List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                       Date toDate,
                                                                       Long doctorId,
                                                                       Long specializationId);

    Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId);

    Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable, Long hospitalId);

    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable,
                                                         Long hospitalId);

    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO,
                                                                            Long hospitalId);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                   Pageable pageable);


    AppointmentLogResponseDTO searchAppointmentLogs(
            AppointmentLogSearchDTO searchRequestDTO, Pageable pageable);

    AppointmentQueueSearchDTO fetchTodayAppointmentQueue(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                         Long hospitalId,
                                                         Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Long hospitalId,
            Pageable pageable);
}
