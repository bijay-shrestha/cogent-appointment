package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Appointment;
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

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS;
import static com.cogent.cogentappointment.admin.query.AppointmentHospitalDepartmentQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_ROOM_WISE;
import static com.cogent.cogentappointment.admin.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.query.TransactionLogQuery.*;
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
    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO) {

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
        Query query = createQuery.apply(entityManager, QUERY_TO_OVER_ALL_APPOINTMENTS(dashBoardRequestDTO.getHospitalId()))
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
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
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
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

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
    public List<HospitalDeptAppointmentStatusResponseDTO> fetchHospitalDeptAppointmentForAppointmentStatusRoomwise(HospitalDeptAppointmentStatusRequestDTO requestDTO) {
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

    private Query getQueryToFetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO) {
        return createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_CANCEL_APPROVALS(searchDTO));
    }

    private Double calculateTotalRefundAmount(AppointmentCancelApprovalSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(searchDTO));

        return (Double) query.getSingleResult();
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        AppointmentPendingApprovalResponseDTO appointmentPendingApprovalResponseDTO =
                new AppointmentPendingApprovalResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPROVALS.apply(searchRequestDTO));

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
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_LOGS.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentLogResponseDTO results = parseQueryResultToAppointmentLogResponse(objects);

        if (results.getAppointmentLogs().isEmpty())
            throw APPOINTMENT_NOT_FOUND.get();

        else {
            results.setTotalItems(totalItems);
            calculateAppointmentStatisticsForAppointmentLog(searchRequestDTO, results);
            return results;
        }
    }

    @Override
    public HospitalDepartmentAppointmentLogResponseDTO searchHospitalDepartmentAppointmentLogs(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO,
            Pageable pageable) {

        HospitalDepartmentAppointmentLogResponseDTO results = new HospitalDepartmentAppointmentLogResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOGS
                .apply(searchRequestDTO))
                .setParameter(APPOINTMENT_SERVICE_TYPE_ID, searchRequestDTO.getServiceTypeId());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDepartmentAppointmentLogDTO> appointmentLogs = transformQueryToResultList
                (query, HospitalDepartmentAppointmentLogDTO.class);
        if (appointmentLogs.isEmpty()) {
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            results.setTotalItems(totalItems);
            results.setAppointmentLogs(appointmentLogs);
            calculateAppointmentStatisticsForHospitalDepartmentAppointmentLog(searchRequestDTO, results);
            return results;
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

    private void calculateAppointmentStatisticsForAppointmentLog(AppointmentLogSearchDTO searchRequestDTO,
                                                                 AppointmentLogResponseDTO appointmentLogResponse) {

        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentDetails(searchRequestDTO);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInAppointmentDetails(searchRequestDTO);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentDetails(searchRequestDTO);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentDetails(searchRequestDTO);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentDetails(searchRequestDTO);

        parseToAppointmentLogResponseDTO(
                appointmentLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentAmount(searchRequestDTO, appointmentLogResponse);
    }

    private void calculateAppointmentStatisticsForHospitalDepartmentAppointmentLog(
            HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO,
            HospitalDepartmentAppointmentLogResponseDTO appointmentLogResponse) {

//        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentDetails(searchRequestDTO);
//
//        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInAppointmentDetails(searchRequestDTO);
//
//        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentDetails(searchRequestDTO);
//
//        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentDetails(searchRequestDTO);
//
//        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
//                getRevenueFromRefundedAppointmentDetails(searchRequestDTO);
//
//        parseToAppointmentLogResponseDTO(
//                appointmentLogResponse,
//                bookedInfo,
//                checkedInInfo,
//                cancelledInfo,
//                refundInfo,
//                revenueFromRefund
//        );
//
//        calculateTotalAppointmentAmount(searchRequestDTO, appointmentLogResponse);
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

    private void calculateTotalAppointmentAmount(AppointmentLogSearchDTO searchRequestDTO,
                                                 AppointmentLogResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(searchRequestDTO));

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

    private BookedAppointmentResponseDTO getBookedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO) {

         /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT(searchRequestDTO));

        List<Object[]> bookedResult = query.getResultList();

         /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO));

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT(searchRequestDTO));

        List<Object[]> checkInResult = query.getResultList();

         /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO));

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO) {

         /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_APPOINTMENT(searchRequestDTO));

        List<Object[]> cancelledResult = query.getResultList();

         /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO));

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO) {

         /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_APPOINTMENT(searchRequestDTO));

        List<Object[]> refundResult = query.getResultList();

         /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO));

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentDetails
            (AppointmentLogSearchDTO searchRequestDTO) {

           /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT(searchRequestDTO));

        List<Object[]> refundResult = query.getResultList();

         /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO));

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private Double calculateTotalRescheduleAmount(AppointmentRescheduleLogSearchDTO searchDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_CALCULATE_TOTAL_RESCHEDULE_AMOUNT(searchDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        return (Double) query.getSingleResult();
    }

}
