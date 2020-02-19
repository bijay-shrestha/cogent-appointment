package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueSearchByTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
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

    List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                       Date toDate,
                                                                       Long doctorId,
                                                                       Long specializationId);

    Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId);

    AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                         Pageable pageable);

    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);


    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable);

    AppointmentLogResponseDTO searchAppointmentLogs(
            AppointmentLogSearchDTO searchRequestDTO, Pageable pageable);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO, Pageable pageable);

    Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO);

    AppointmentQueueSearchDTO fetchTodayAppointmentQueue(AppointmentQueueRequestDTO appointmentQueueRequestDTO, Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(AppointmentQueueRequestDTO appointmentQueueRequestDTO, Pageable pageable);
}
