package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentTransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.HospitalDepartmentAppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.HospitalDepartmentAppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.HospitalDepartmentAppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
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

    AppointmentRefundResponseDTO fetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO,
                                                                 Pageable pageable);

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId);

    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO);


    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable);

    AppointmentPendingApprovalDetailResponseDTO fetchDetailsByAppointmentId(Long appointmentId);

    AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO, Pageable pageable);

    HospitalDepartmentAppointmentLogResponseDTO searchHospitalDepartmentAppointmentLogs(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO,
            Pageable pageable);

    TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO, Pageable pageable);

    HospitalDepartmentTransactionLogResponseDTO searchHospitalDepartmentTransactionLogs(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO,
            Pageable pageable);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                   Pageable pageable);

    HospitalDepartmentAppointmentRescheduleLogResponseDTO searchHospitalDepartmentRescheduleLogs(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO rescheduleDTO,
            Pageable pageable);

    Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countFollowUpPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO);

    List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                       Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime
            (AppointmentQueueRequestDTO appointmentQueueRequestDTO, Pageable pageable);

    Appointment fetchCancelledAppointmentDetails(RefundStatusRequestDTO requestDTO);

    List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatusRoomwise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);
}
