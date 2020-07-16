package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.count.HospitalDeptAppointmentStatusCountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.CancelledHospitalDeptAppointmentSearchDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentDetailsForStatus;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.count.AppointmentCountWithStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentDetailsForStatus;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.refund.CancelledHospitalDeptAppointmentResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.refund.HospitalDeptCancelledAppointmentDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyDoctorWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentDoctorInfo;
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

    Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    Long countFollowUpPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId);

    AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable, Long hospitalId);

    AppointmentPendingApprovalDetailResponseDTO fetchDetailsByAppointmentId(Long appointmentId);

    AppointmentRefundResponseDTO fetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO,
                                                                 Pageable pageable,
                                                                 Long hospitalId);

    AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId);

    List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO,
                                                                            Long hospitalId);

    AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                   Pageable pageable,
                                                                   Long hospitalId);

    AppointmentLogResponseDTO searchDoctorAppointmentLogs(
            AppointmentLogSearchDTO searchRequestDTO, Pageable pageable,
            Long hospitalId, String appointmentServiceTypeCode);

    AppointmentLogResponseDTO searchHospitalDepartmentAppointmentLogs(
            AppointmentLogSearchDTO searchRequestDTO, Pageable pageable,
            Long hospitalId, String appointmentServiceTypeCode);

    TransactionLogResponseDTO searchDoctorTransactionLogs(
            TransactionLogSearchDTO searchRequestDTO,
            Pageable pageable,
            Long hospitalId,
            String appointmentServiceTypeCode);

    TransactionLogResponseDTO searchHospitalDepartmentTransactionLogs(
            TransactionLogSearchDTO searchRequestDTO,
            Pageable pageable,
            Long hospitalId,
            String appointmentServiceTypeCode);

    List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                       Long hospitalId,
                                                       Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Long hospitalId,
            Pageable pageable);

    Appointment fetchCancelledAppointmentDetails(RefundStatusRequestDTO requestDTO);

    List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatusRoomWise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            Pageable pageable,
            Long hospitalId);

    AppointmentHospitalDepartmentCheckInDetailResponseDTO fetchPendingHospitalDeptAppointmentDetail(
            Long appointmentId, Long hospitalId);

    ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO fetchAppointmentDetailForHospitalDeptCheckIn(
            Long appointmentId, Long hospitalId);

    ThirdPartyDoctorWiseAppointmentCheckInDTO fetchAppointmentDetailForDoctorWiseApptCheckIn(
            Long appointmentId, Long hospitalId);


    AppointmentDoctorInfo getPreviousAppointmentDoctorAndSpecialization(Long appointmentId);

    HospitalDeptAppointmentDetailsForStatus fetchHospitalDeptAppointmentByApptNumber(String apptNumber);

    AppointmentDetailsForStatus fetchAppointmentByApptNumber(String apptNumber,
                                                             String appointmentServiceTypeCode);

    CancelledHospitalDeptAppointmentResponseDTO fetchCancelledHospitalDeptAppointments(
            CancelledHospitalDeptAppointmentSearchDTO searchDTO,
            Pageable pageable);

    List<AppointmentCountWithStatusDTO> getAppointmentCountWithStatus(HospitalDeptAppointmentStatusCountRequestDTO requestDTO);

    Long getAppointmentFollowUpCount(HospitalDeptAppointmentStatusCountRequestDTO requestDTO);

    HospitalDeptCancelledAppointmentDetailResponseDTO fetchCancelledAppointmentDetail(Long appointmentId);
}
