package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.count.HospitalDeptAppointmentStatusCountRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.CancelledHospitalDeptAppointmentSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentDetailsForStatus;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count.AppointmentCountWithStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentDetailsForStatus;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.refund.CancelledHospitalDeptAppointmentDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.refund.CancelledHospitalDeptAppointmentResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.refund.HospitalDeptCancelledAppointmentDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyDoctorWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.commons.exception.BadRequestException;
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

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AppointmentTransferMessage.APPOINTMENT_DOCTOR_INFORMATION_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NO_SERVICE_TYPE_FOUND;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentDoctorInfoQuery.QUERY_TO_FETCH_APPOINTMENT_DETAIL_FOR_DOCTOR_WISE_CHECK_IN;
import static com.cogent.cogentappointment.admin.query.AppointmentDoctorInfoQuery.QUERY_TO_GET_CURRENT_APPOINTMENT_DOCTOR_INFO;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentLogQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentTransactionLogQuery.QUERY_TO_FETCH_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP;
import static com.cogent.cogentappointment.admin.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentStatusCountQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_COUNT;
import static com.cogent.cogentappointment.admin.query.AppointmentStatusCountQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_FOLLOW_UP_COUNT;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentLogUtils.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDateInString;
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

    /*USED IN DOCTOR DUTY ROSTER SHIFT WISE TO VALIDATE IF APPOINTMENT EXISTS ON SELECTED OVERRIDE DATE*/
    @Override
    public Long fetchBookedAppointmentCount(Date date, Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_COUNT)
                .setParameter(DATE, utilDateToSqlDate(date))
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
                .setParameter(FROM_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countFollowUpPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_FOLLOW_UP_APPOINTMENT(
                dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT(
                dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager,
                QUERY_TO_OVER_ALL_APPOINTMENTS(dashBoardRequestDTO.getHospitalId()))
                .setParameter(FROM_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDateInString(dashBoardRequestDTO.getToDate()));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              Pageable pageable) {

        Query query = getQueryForAppointmentQueue(appointmentQueueRequestDTO);

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

    private Query getQueryForAppointmentQueue(AppointmentQueueRequestDTO appointmentQueueRequestDTO) {

        switch (appointmentQueueRequestDTO.getAppointmentServiceType().trim().toUpperCase()) {
            case DOCTOR_CONSULTATION_CODE:
                return createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_QUEUE
                        .apply(appointmentQueueRequestDTO))
                        .setParameter(DATE, utilDateToSqlDate(appointmentQueueRequestDTO.getDate()))
                        .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentQueueRequestDTO.getAppointmentServiceType());

            case DEPARTMENT_CONSULTATION_CODE:
                return createQuery.apply(entityManager, QUERY_TO_FETCH_DEPARTMENT_APPOINTMENT_QUEUE
                        .apply(appointmentQueueRequestDTO))
                        .setParameter(DATE, utilDateToSqlDate(appointmentQueueRequestDTO.getDate()))
                        .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentQueueRequestDTO.getAppointmentServiceType());

            default:
                throw new NoContentFoundException(NO_SERVICE_TYPE_FOUND);
        }
    }

    @Override
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_APPOINTMENT_QUEUE.apply(appointmentQueueRequestDTO))
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
                QUERY_TO_FETCH_DOCTOR_APPOINTMENT_LOGS.apply(searchRequestDTO))
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
    public TransactionLogResponseDTO searchDoctorAppointmentTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                                            Pageable pageable,
                                                                            String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TRANSACTION_LOGS.apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<TransactionLogDTO> transactionLogs = transformQueryToResultList(query, TransactionLogDTO.class);

        if (transactionLogs.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            TransactionLogResponseDTO responseDTO = new TransactionLogResponseDTO();
            responseDTO.setTransactionLogs(transactionLogs);
            responseDTO.setTotalItems(totalItems);
            calculateDoctorAppointmentStatisticsForTransactionLog(
                    searchRequestDTO, responseDTO, appointmentServiceTypeCode);
            return responseDTO;
        }
    }

    @Override
    public TransactionLogResponseDTO searchHospitalDepartmentTransactionLogs(
            TransactionLogSearchDTO searchRequestDTO,
            Pageable pageable,
            String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOGS
                .apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, searchRequestDTO.getAppointmentServiceTypeCode());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<TransactionLogDTO> transactionLogs = transformQueryToResultList(query, TransactionLogDTO.class);

        if (transactionLogs.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            TransactionLogResponseDTO responseDTO = new TransactionLogResponseDTO();
            responseDTO.setTotalItems(totalItems);
            responseDTO.setTransactionLogs(transactionLogs);
            calculateHospitalDepartmentAppointmentStatisticsForTransactionLog(
                    searchRequestDTO, responseDTO, appointmentServiceTypeCode);
            return responseDTO;
        }
    }

    @Override
    public AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                          Pageable pageable) {

        String appointmentServiceTypeCode = rescheduleDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        switch (appointmentServiceTypeCode) {

            case DOCTOR_CONSULTATION_CODE:
                return fetchRescheduleDoctorAppointment(rescheduleDTO, pageable, appointmentServiceTypeCode);

            case DEPARTMENT_CONSULTATION_CODE:
                return fetchRescheduleHospitalDepartmentAppointment(rescheduleDTO, pageable,
                        appointmentServiceTypeCode);

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE,
                        appointmentServiceTypeCode));
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

    @Override
    public CancelledHospitalDeptAppointmentResponseDTO fetchCancelledHospitalDeptAppointments(CancelledHospitalDeptAppointmentSearchDTO searchDTO,
                                                                                              Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPT_APPOINTMENTS(searchDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<CancelledHospitalDeptAppointmentDTO> cancelledAppointments =
                transformQueryToResultList(query, CancelledHospitalDeptAppointmentDTO.class);

        if (cancelledAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            Double totalRefundAmount = calculateTotalHospitalDeptRefundAmount(searchDTO);
            return CancelledHospitalDeptAppointmentResponseDTO.builder()
                    .cancelledAppointments(cancelledAppointments)
                    .totalItems(totalItems)
                    .totalRefundAmount(totalRefundAmount)
                    .build();
        }


    }

    @Override
    public HospitalDeptCancelledAppointmentDetailResponseDTO fetchCancelledAppointmentDetail(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_HOSPITAL_DEPT_APPOINTMENTS_DETAIL_BY_APPOINTMENT_ID)
                .setParameter(APPOINTMENT_ID, appointmentId);

        try {
            return transformQueryToSingleResult(query, HospitalDeptCancelledAppointmentDetailResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_NOT_FOUND.get();
        }
    }

    @Override
    public List<AppointmentCountWithStatusDTO> getAppointmentCountWithStatus(HospitalDeptAppointmentStatusCountRequestDTO requestDTO) {

//        List<AppointmentCountWithStatusDTO> response=  entityManager.createNativeQuery(
//                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_COUNT(requestDTO),
//                AppointmentCountWithStatusDTO.class)
//                .setParameter(FROM_DATE,requestDTO.getFromDate())
//                .setParameter(TO_DATE,requestDTO.getToDate())
//                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE,"DEP")
//                .getResultList();

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_COUNT(requestDTO))
                .setParameter(FROM_DATE, requestDTO.getFromDate())
                .setParameter(TO_DATE, requestDTO.getToDate())
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, "DEP");

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        List<AppointmentCountWithStatusDTO> response = transformQueryToResultList(query, AppointmentCountWithStatusDTO.class);

        return response;
    }

    @Override
    public Long getAppointmentFollowUpCount(HospitalDeptAppointmentStatusCountRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_FOLLOW_UP_COUNT(requestDTO))
                .setParameter(FROM_DATE, requestDTO.getFromDate())
                .setParameter(TO_DATE, requestDTO.getToDate())
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, "DEP");

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());


        try {
            return (Long) query.getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
    }

    private Query getQueryToFetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO) {
        return createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_CANCEL_APPROVALS(searchDTO));
    }

    private Double calculateTotalRefundAmount(AppointmentCancelApprovalSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(searchDTO));

        return (Double) query.getSingleResult();
    }


    private Double calculateTotalHospitalDeptRefundAmount(CancelledHospitalDeptAppointmentSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_HOSPITAL_DEPARTMENT_REFUND_AMOUNT(searchDTO));

        try {
            return (Double) query.getSingleResult();
        } catch (NoResultException e) {
            return 0D;
        }
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

    private void calculateDoctorAppointmentStatisticsForTransactionLog(TransactionLogSearchDTO searchRequestDTO,
                                                                       TransactionLogResponseDTO txnLogResponse,
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

        parseToTxnLogResponseDTO(
                txnLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentAmount(searchRequestDTO, txnLogResponse, appointmentServiceTypeCode);
    }

    private void calculateHospitalDepartmentAppointmentStatisticsForTransactionLog(
            TransactionLogSearchDTO searchRequestDTO,
            TransactionLogResponseDTO txnLogResponse,
            String appointmentServiceTypeCode) {

        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentTxnDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInHospitalDepartmentAppointmentTxnDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentTxnDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentTxnDetails(
                searchRequestDTO, appointmentServiceTypeCode);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentTxnDetails(searchRequestDTO, appointmentServiceTypeCode);

        parseToTxnLogResponseDTO(
                txnLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentTxnAmount(searchRequestDTO, txnLogResponse, appointmentServiceTypeCode);
    }

    private void calculateTotalAppointmentAmount(AppointmentLogSearchDTO searchRequestDTO,
                                                 AppointmentLogResponseDTO responseDTO,
                                                 String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private BookedAppointmentResponseDTO getBookedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                     String appointmentServiceTypeCode) {

        /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedResult = query.getResultList();

        /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInAppointmentDetails(
            TransactionLogSearchDTO searchRequestDTO,
            String appointmentServiceTypeCode) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInResult = query.getResultList();

        /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                           String appointmentServiceTypeCode) {

        /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> cancelledResult = query.getResultList();

        /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                       String appointmentServiceTypeCode) {

        /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundResult = query.getResultList();

        /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentDetails
            (TransactionLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

        /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);
        ;

        List<Object[]> refundResult = query.getResultList();

        /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalAppointmentAmount(TransactionLogSearchDTO searchRequestDTO,
                                                 TransactionLogResponseDTO responseDTO,
                                                 String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT_FOR_TRANSACTION_LOG(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

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
            TransactionLogSearchDTO searchRequestDTO,
            String appointmentServiceTypeCode) {

        /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> bookedResult = query.getResultList();

        /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, searchRequestDTO.getAppointmentServiceTypeCode());

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInHospitalDepartmentAppointmentTxnDetails
            (TransactionLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInResult = query.getResultList();

        /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentTxnDetails
            (TransactionLogSearchDTO searchRequestDTO,
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

    private RefundAppointmentResponseDTO getRefundedAppointmentTxnDetails(
            TransactionLogSearchDTO searchRequestDTO,
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

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentTxnDetails
            (TransactionLogSearchDTO searchRequestDTO,
             String appointmentServiceTypeCode) {

        /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundResult = query.getResultList();

        /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_HOSPITAL_DEPARTMENT_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalAppointmentTxnAmount(TransactionLogSearchDTO searchRequestDTO,
                                                    TransactionLogResponseDTO responseDTO,
                                                    String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_TOTAL_HOSPITAL_APPOINTMENT_APPOINTMENT_AMOUNT(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private AppointmentRescheduleLogResponseDTO fetchRescheduleDoctorAppointment(
            AppointmentRescheduleLogSearchDTO rescheduleDTO,
            Pageable pageable,
            String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_RESCHEDULE_APPOINTMENT_LOGS.apply(rescheduleDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentRescheduleLogDTO> rescheduleAppointments =
                transformQueryToResultList(query, AppointmentRescheduleLogDTO.class);

        if (rescheduleAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            Double totalRescheduleAmount = calculateTotalRescheduleDoctorAppointmentAmount(
                    rescheduleDTO, appointmentServiceTypeCode);

            return AppointmentRescheduleLogResponseDTO.builder()
                    .appointmentRescheduleLogDTOS(rescheduleAppointments)
                    .totalItems(totalItems)
                    .totalAmount(totalRescheduleAmount)
                    .build();
        }
    }

    private AppointmentRescheduleLogResponseDTO fetchRescheduleHospitalDepartmentAppointment(
            AppointmentRescheduleLogSearchDTO rescheduleDTO,
            Pageable pageable,
            String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_RESCHEDULE_APPOINTMENT_LOGS.apply(rescheduleDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentRescheduleLogDTO> rescheduleAppointments =
                transformQueryToResultList(query, AppointmentRescheduleLogDTO.class);

        if (rescheduleAppointments.isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            Double totalRescheduleAmount = calculateTotalRescheduleHospitalDepartmentAppointmentAmount(
                    rescheduleDTO, appointmentServiceTypeCode);

            return AppointmentRescheduleLogResponseDTO.builder()
                    .appointmentRescheduleLogDTOS(rescheduleAppointments)
                    .totalItems(totalItems)
                    .totalAmount(totalRescheduleAmount)
                    .build();
        }
    }

    private Double calculateTotalRescheduleDoctorAppointmentAmount(AppointmentRescheduleLogSearchDTO searchDTO,
                                                                   String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_TOTAL_RESCHEDULE_AMOUNT(searchDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        return (Double) query.getSingleResult();
    }

    private Double calculateTotalRescheduleHospitalDepartmentAppointmentAmount(
            AppointmentRescheduleLogSearchDTO searchDTO,
            String appointmentServiceTypeCode) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_CALCULATE_TOTAL_HOSPITAL_DEPT_RESCHEDULE_AMOUNT(searchDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode);

        return (Double) query.getSingleResult();
    }
}
