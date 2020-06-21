package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentTransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
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
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentDetailsForStatus;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentDetailsForStatus;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.HospitalDepartmentAppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.HospitalDepartmentAppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.TransactionLogQuery;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyDoctorWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentDoctorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AppointmentTransferMessage.APPOINTMENT_DOCTOR_INFORMATION_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentDoctorInfoQuery.QUERY_TO_FETCH_APPOINTMENT_DETAIL_FOR_DOCTOR_WISE_CHECK_IN;
import static com.cogent.cogentappointment.admin.query.AppointmentDoctorInfoQuery.QUERY_TO_GET_CURRENT_APPOINTMENT_DOCTOR_INFO;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentRevenueQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.*;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.utils.AppointmentLogUtils.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.admin.utils.TransactionLogUtils.parseQueryResultToTransactionLogResponse;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /*USED WHILE SAVING DOCTOR DUTY ROSTER TO VALIDATE IF APPOINTMENT EXISTS*/
    @Override
    public List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                              Date toDate,
                                                                              Long doctorId,
                                                                              Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        return transformQueryToResultList(query, AppointmentBookedDateResponseDTO.class);
    }

    /*USED IN DOCTOR DUTY ROSTER TO VALIDATE IF APPOINTMENT EXISTS*/
    @Override
    public Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        return (Long) query.getSingleResult();
    }

    @Override
    public AppointmentRefundResponseDTO fetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO,
                                                                        Pageable pageable) {

        Query query = getQueryToFetchAppointmentCancelApprovals(searchDTO);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentRefundDTO> refundAppointments = transformQueryToResultList(query, AppointmentRefundDTO.class);

        if (refundAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            Double totalRefundAmount = calculateTotalRefundAmount(searchDTO);
            return AppointmentRefundResponseDTO.builder()
                    .refundAppointments(refundAppointments)
                    .totalItems(totalItems)
                    .totalRefundAmount(totalRefundAmount)
                    .build();
        }
    }

    @Override
    public AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_REFUNDED_DETAIL_BY_ID)
                .setParameter(APPOINTMENT_ID, appointmentId);
        try {
            return transformQueryToSingleResult(query, AppointmentRefundDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_DETAILS_NOT_FOUND.apply(appointmentId);
        }
    }

    @Override
    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
            AppointmentStatusRequestDTO requestDTO) {

        Query query = createNativeQuery.apply(entityManager,
                QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToAppointmentStatusResponseDTOS(results);
    }

    @Override
    public Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_REGISTERED_APPOINTMENT(
                dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countFollowUpPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_FOLLOW_UP_APPOINTMENT(
                dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT(
                dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager,
                QUERY_TO_OVER_ALL_APPOINTMENTS(dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_QUEUE
                .apply(appointmentQueueRequestDTO))
                .setParameter(DATE, utilDateToSqlDate(appointmentQueueRequestDTO.getDate()));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentQueueDTO> results = transformQueryToResultList(query, AppointmentQueueDTO.class);

        if (results.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_QUEUE.apply(appointmentQueueRequestDTO))
                .setParameter(DATE, utilDateToSqlDate(appointmentQueueRequestDTO.getDate()));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        Map<String, List<AppointmentQueueDTO>> results = parseQueryResultToAppointmentQueueForTodayByTimeResponse(objects);

        return results;
    }

    @Override
    public Appointment fetchCancelledAppointmentDetails(RefundStatusRequestDTO requestDTO) {
        try {
            Appointment appointment = entityManager.createQuery(QUERY_TO_GET_CANCELLED_APPOINTMENT,
                    Appointment.class)
                    .setParameter(ESEWA_ID, requestDTO.getEsewaId())
                    .setParameter(TRANSACTION_NUMBER, requestDTO.getTransactionNumber())
                    .getSingleResult();

            return appointment;
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT);
            throw APPOINTMENT_NOT_FOUND.get();
        }
    }

    @Override
    public List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatus
            (HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        Query query = createNativeQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, "DEP");

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, requestDTO.getHospitalDepartmentRoomInfoId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToHospitalDeptAppointmentStatusResponseDTOS(results);
    }

    @Override
    public HospitalDeptAppointmentDetailsForStatus fetchHospitalDeptAppointmentByApptNumber(String appointmentNumber,
                                                                                            String appointmentServiceTypeCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_BY_APPT_NUMBER)
                .setParameter(APPOINTMENT_NUMBER, appointmentNumber)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        try {
            HospitalDeptAppointmentDetailsForStatus responseDTO = transformQueryToSingleResult(query,
                    HospitalDeptAppointmentDetailsForStatus.class);
            return responseDTO;
        } catch (NoResultException e) {
            throw APPOINTMENT_NOT_FOUND.get();
        }
    }

    @Override
    public AppointmentDetailsForStatus fetchAppointmentByApptNumber(String appointmentNumber, String appointmentServiceTypeCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_BY_APPT_NUMBER)
                .setParameter(APPOINTMENT_NUMBER, appointmentNumber)
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        try {
            AppointmentDetailsForStatus responseDTO = transformQueryToSingleResult(query, AppointmentDetailsForStatus.class);
            return responseDTO;
        } catch (NoResultException e) {
            throw APPOINTMENT_NOT_FOUND.get();
        }
    }

    @Override
    public List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatusRoomwise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        Query query = createNativeQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_ROOM_WISE(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, requestDTO.getHospitalDepartmentRoomInfoId());

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToHospitalDeptAppointmentStatusResponseDTOS(results);
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        AppointmentPendingApprovalResponseDTO appointmentPendingApprovalResponseDTO =
                new AppointmentPendingApprovalResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPROVALS.apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, DOCTOR_CONSULTATION_CODE);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentPendingApprovalDTO> appointmentPendingApprovalDTOS =
                transformQueryToResultList(query, AppointmentPendingApprovalDTO.class);

        if (appointmentPendingApprovalDTOS.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            appointmentPendingApprovalResponseDTO.setPendingAppointmentApprovals(appointmentPendingApprovalDTOS);
            appointmentPendingApprovalResponseDTO.setTotalItems(totalItems);
            return appointmentPendingApprovalResponseDTO;
        }
    }

    @Override
    public AppointmentPendingApprovalDetailResponseDTO fetchDetailsByAppointmentId(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPROVAL_DETAIL_BY_ID)
                .setParameter(APPOINTMENT_ID, appointmentId);
        try {
            return transformQueryToSingleResult(query, AppointmentPendingApprovalDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_DETAILS_NOT_FOUND.apply(appointmentId);
        }
    }

    @Override
    public AppointmentLogResponseDTO searchDoctorAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                                 Pageable pageable,
                                                                 String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_APPOINTMENT_LOGS.apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentLogDTO> appointmentLogs = transformQueryToResultList(query, AppointmentLogDTO.class);

        if (appointmentLogs.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            AppointmentLogResponseDTO responseDTO = new AppointmentLogResponseDTO();
            responseDTO.setAppointmentLogs(appointmentLogs);
            responseDTO.setTotalItems(totalItems);
            calculateAppointmentStatisticsForAppointmentLog(searchRequestDTO, responseDTO, appointmentServiceTypeCode);
            return responseDTO;
        }
    }

    @Override
    public AppointmentLogResponseDTO searchHospitalDepartmentAppointmentLogs(
            AppointmentLogSearchDTO searchRequestDTO,
            Pageable pageable,
            String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOGS.apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentLogDTO> appointmentLogs = transformQueryToResultList(query, AppointmentLogDTO.class);

        if (appointmentLogs.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            AppointmentLogResponseDTO responseDTO = new AppointmentLogResponseDTO();
            responseDTO.setAppointmentLogs(appointmentLogs);
            responseDTO.setTotalItems(totalItems);
            calculateHospitalDepartmentAppointmentStatistics(
                    searchRequestDTO, responseDTO, appointmentServiceTypeCode);
            return responseDTO;
        }
    }

    @Override
    public TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TRANSACTION_LOGS.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        TransactionLogResponseDTO results = parseQueryResultToTransactionLogResponse(objects);

        if (results.getTransactionLogs().isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            results.setTotalItems(totalItems);
            calculateAppointmentStatisticsForTransactionLog(searchRequestDTO, results);
            return results;
        }
    }

    @Override
    public HospitalDepartmentTransactionLogResponseDTO searchHospitalDepartmentTransactionLogs(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO, Pageable pageable) {

        HospitalDepartmentTransactionLogResponseDTO results = new HospitalDepartmentTransactionLogResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOGS
                .apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDepartmentTransactionLogDTO> appointmentLogs = transformQueryToResultList
                (query, HospitalDepartmentTransactionLogDTO.class);
        if (appointmentLogs.isEmpty()) {
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            results.setTotalItems(totalItems);
            results.setTransactionLogs(appointmentLogs);
            calculateAppointmentStatisticsForHospitalDepartmentTransactionLog(searchRequestDTO, results);
            return results;
        }

    }

    @Override
    public AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                          Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_RESCHEDULE_APPOINTMENT_LOGS.apply(rescheduleDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(rescheduleDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(rescheduleDTO.getToDate()));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentRescheduleLogDTO> rescheduleAppointments =
                transformQueryToResultList(query, AppointmentRescheduleLogDTO.class);

        if (rescheduleAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            Double totalRescheduleAmount = calculateTotalRescheduleAmount(rescheduleDTO);

            return AppointmentRescheduleLogResponseDTO.builder()
                    .appointmentRescheduleLogDTOS(rescheduleAppointments)
                    .totalItems(totalItems)
                    .totalAmount(totalRescheduleAmount)
                    .build();
        }
    }

    @Override
    public HospitalDepartmentAppointmentRescheduleLogResponseDTO searchHospitalDepartmentRescheduleLogs(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO rescheduleDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_RESCHEDULE_APPOINTMENT_LOGS
                .apply(rescheduleDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(rescheduleDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(rescheduleDTO.getToDate()))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, rescheduleDTO.getServiceTypeId());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDepartmentAppointmentRescheduleLogDTO> rescheduleAppointments =
                transformQueryToResultList(query, HospitalDepartmentAppointmentRescheduleLogDTO.class);

        if (rescheduleAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            Double totalRescheduleAmount = calculateTotalHospitalDepartmentApptRescheduleAmount(rescheduleDTO);

            return HospitalDepartmentAppointmentRescheduleLogResponseDTO.builder()
                    .appointmentRescheduleLogDTOS(rescheduleAppointments)
                    .totalItems(totalItems)
                    .totalAmount(totalRescheduleAmount)
                    .build();
        }
    }

    @Override
    public List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            Pageable pageable) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENTS.apply(searchDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentHospitalDepartmentCheckInResponseDTO> pendingAppointments =
                transformQueryToResultList(query, AppointmentHospitalDepartmentCheckInResponseDTO.class);

        if (pendingAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        pendingAppointments.get(0).setTotalItems(totalItems);
        return pendingAppointments;
    }

    @Override
    public AppointmentHospitalDepartmentCheckInDetailResponseDTO fetchPendingHospitalDeptAppointmentDetail(
            Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPOINTMENT_DETAIL)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            return transformQueryToSingleResult(query, AppointmentHospitalDepartmentCheckInDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
        }
    }

    @Override
    public AppointmentDoctorInfo getPreviousAppointmentDoctorAndSpecialization(Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_GET_CURRENT_APPOINTMENT_DOCTOR_INFO)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            AppointmentDoctorInfo appointmentDoctorInfo = transformQueryToSingleResult(query, AppointmentDoctorInfo.class);
            return appointmentDoctorInfo;
        } catch (NoContentFoundException e) {
            log.error(APPOINTMENT_DOCTOR_INFORMATION_NOT_FOUND);
            throw new NoContentFoundException(APPOINTMENT_DOCTOR_INFORMATION_NOT_FOUND,
                    APPOINTMENT_DOCTOR_INFORMATION_NOT_FOUND);
        }
    }

    @Override
    public ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO fetchAppointmentDetailForHospitalDeptCheckIn(
            Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAIL_FOR_HOSPITAL_DEPT_CHECK_IN)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            return transformQueryToSingleResult(query, ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
        }
    }

    @Override
    public ThirdPartyDoctorWiseAppointmentCheckInDTO fetchAppointmentDetailForDoctorWiseApptCheckIn(
            Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAIL_FOR_DOCTOR_WISE_CHECK_IN)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            return transformQueryToSingleResult(query, ThirdPartyDoctorWiseAppointmentCheckInDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
        }
    }

    private Query getQueryToFetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO) {
        return createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_CANCEL_APPROVALS(searchDTO));
    }

    private Double calculateTotalRefundAmount(AppointmentCancelApprovalSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(searchDTO));

        return (Double) query.getSingleResult();
    }

    private void calculateAppointmentStatisticsForAppointmentLog(AppointmentLogSearchDTO searchRequestDTO,
                                                                 AppointmentLogResponseDTO appointmentLogResponse,
                                                                 String appointmentServiceTypeCode) {

        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentDetails(searchRequestDTO, appointmentServiceTypeCode);

        parseToAppointmentLogResponseDTO(
                appointmentLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentAmount(searchRequestDTO, appointmentLogResponse, appointmentServiceTypeCode);
    }

    private void calculateHospitalDepartmentAppointmentStatistics(
            AppointmentLogSearchDTO searchRequestDTO,
            AppointmentLogResponseDTO appointmentLogResponse,
            String appointmentServiceTypeCode) {

        BookedAppointmentResponseDTO bookedInfo =
                getBookedHospitalDepartmentAppointmentDetails(searchRequestDTO, appointmentServiceTypeCode);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInHospitalDepartmentAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledHospitalDepartmentAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        RefundAppointmentResponseDTO refundInfo = getRefundedHospitalDepartmentAppointmentDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedHospitalDepartmentAppointmentDetails(searchRequestDTO, appointmentServiceTypeCode);

        parseToAppointmentLogResponseDTO(
                appointmentLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalHospitalDepartmentAppointmentAmount(
                searchRequestDTO, appointmentLogResponse, appointmentServiceTypeCode);
    }

    private void calculateAppointmentStatisticsForTransactionLog(TransactionLogSearchDTO searchRequestDTO,
                                                                 TransactionLogResponseDTO txnLogResponse) {

        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentDetails(searchRequestDTO);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInAppointmentDetails(searchRequestDTO);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentDetails(searchRequestDTO);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentDetails(searchRequestDTO);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentDetails(searchRequestDTO);

        parseToTxnLogResponseDTO(
                txnLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentAmount(searchRequestDTO, txnLogResponse);
    }

    private void calculateAppointmentStatisticsForHospitalDepartmentTransactionLog(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO,
            HospitalDepartmentTransactionLogResponseDTO txnLogResponse) {


        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentTxnDetails(searchRequestDTO);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInHospitalDepartmentAppointmentTxnDetails(searchRequestDTO);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentTxnDetails(searchRequestDTO);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentTxnDetails(searchRequestDTO);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentTxnDetails(searchRequestDTO);

        parseToHospitalDeptTxnLogResponseDTO(
                txnLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentTxnAmount(searchRequestDTO, txnLogResponse);
    }

    private void calculateTotalAppointmentAmount(AppointmentLogSearchDTO searchRequestDTO,
                                                 AppointmentLogResponseDTO responseDTO,
                                                 String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);
        ;

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private BookedAppointmentResponseDTO getBookedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO) {

        /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> bookedResult = query.getResultList();

        /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInAppointmentDetails(TransactionLogSearchDTO searchRequestDTO) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> checkInResult = query.getResultList();

        /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }


    private CancelledAppointmentResponseDTO getCancelledAppointmentDetails(TransactionLogSearchDTO searchRequestDTO) {

        /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> cancelledResult = query.getResultList();

        /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO) {

        /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> refundResult = query.getResultList();

        /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentDetails
            (TransactionLogSearchDTO searchRequestDTO) {

        /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> refundResult = query.getResultList();

        /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO));

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalAppointmentAmount(TransactionLogSearchDTO searchRequestDTO,
                                                 TransactionLogResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT_FOR_TRANSACTION_LOG(searchRequestDTO));

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_DETAILS_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException
                (AppointmentPendingApprovalDetailResponseDTO.class, "appointmentId", appointmentId.toString());
    };

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT);
        throw new NoContentFoundException(Appointment.class);
    };

    private BookedAppointmentResponseDTO getBookedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                     String appointmentServiceTypeCode) {

        /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedResult = query.getResultList();

        /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                           String appointmentServiceTypeCode) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInResult = query.getResultList();

        /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                           String appointmentServiceTypeCode) {

        /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> cancelledResult = query.getResultList();

        /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                       String appointmentServiceTypeCode) {

        /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundResult = query.getResultList();

        /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentDetails
            (AppointmentLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

        /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundResult = query.getResultList();

        /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private Double calculateTotalRescheduleAmount(AppointmentRescheduleLogSearchDTO searchDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_TOTAL_RESCHEDULE_AMOUNT(searchDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        return (Double) query.getSingleResult();
    }

    private Double calculateTotalHospitalDepartmentApptRescheduleAmount(
            HospitalDepartmentAppointmentRescheduleLogSearchDTO searchDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_CALCULATE_TOTAL_HOSPITAL_DEPARTMENT_APPT_RESCHEDULE_AMOUNT(searchDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchDTO.getServiceTypeId());

        return (Double) query.getSingleResult();
    }

    private BookedAppointmentResponseDTO getBookedHospitalDepartmentAppointmentDetails(
            AppointmentLogSearchDTO searchRequestDTO,
            String appointmentServiceTypeCode) {

         /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedResult = query.getResultList();

         /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInHospitalDepartmentAppointmentDetails
            (AppointmentLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

         /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInResult = query.getResultList();

         /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledHospitalDepartmentAppointmentDetails
            (AppointmentLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

         /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> cancelledResult = query.getResultList();

         /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedHospitalDepartmentAppointmentDetails(
            AppointmentLogSearchDTO searchRequestDTO,
            String appointmentServiceTypeCode) {

         /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundResult = query.getResultList();

         /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedHospitalDepartmentAppointmentDetails
            (AppointmentLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

           /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundResult = query.getResultList();

         /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_FROM_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalHospitalDepartmentAppointmentAmount(AppointmentLogSearchDTO searchRequestDTO,
                                                                   AppointmentLogResponseDTO responseDTO,
                                                                   String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_TOTAL_HOSPITAL_DEPARTMENT_APPOINTMENT_AMOUNT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private BookedAppointmentResponseDTO getBookedAppointmentTxnDetails(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO) {

         /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, TransactionLogQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> bookedResult = query.getResultList();

         /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                TransactionLogQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInHospitalDepartmentAppointmentTxnDetails
            (HospitalDepartmentTransactionLogSearchDTO searchRequestDTO) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> checkInResult = query.getResultList();

         /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentTxnDetails
            (HospitalDepartmentTransactionLogSearchDTO searchRequestDTO) {

         /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> cancelledResult = query.getResultList();

         /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentTxnDetails(
            HospitalDepartmentTransactionLogSearchDTO searchRequestDTO) {

         /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> refundResult = query.getResultList();

         /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentTxnDetails
            (HospitalDepartmentTransactionLogSearchDTO searchRequestDTO) {

              /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> refundResult = query.getResultList();

         /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalAppointmentTxnAmount(HospitalDepartmentTransactionLogSearchDTO searchRequestDTO,
                                                    HospitalDepartmentTransactionLogResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_TOTAL_HOSPITAL_APPOINTMENT_APPOINTMENT_AMOUNT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };


}
