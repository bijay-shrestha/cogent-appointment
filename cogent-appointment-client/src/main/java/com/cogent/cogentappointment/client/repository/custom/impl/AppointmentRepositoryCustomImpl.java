package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.*;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.client.utils.AppointmentUtils;
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

import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.client.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.client.query.AppointmentQuery.QUERY_TO_FETCH_REFUND_AMOUNT;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.client.query.TransactionLogQuery.*;
import static com.cogent.cogentappointment.client.utils.AppointmentLogUtils.*;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.client.utils.TransactionLogUtils.parseQueryResultToTransactionLogResponse;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                            Long doctorId, Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_APPOINTMENT_EXISTS)
                .setParameter(APPOINTMENT_DATE, utilDateToSqlDate(appointmentDate))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(APPOINTMENT_TIME, appointmentTime);

        return (Long) query.getSingleResult();
    }

    /*USED IN APPOINTMENT CHECK AVAILABILITY*/
    @Override
    public List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT)
                .setParameter(DATE, utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AppointmentBookedTimeResponseDTO.class);
    }

    /*eg. 2076-10-10 lies in between 2076-04-01 to 2077-03-31 Fiscal Year ie. 2076/2077*/
    @Override
    public String generateAppointmentNumber(String nepaliCreatedDate,
                                            Long hospitalId) {

        int year = getYearFromNepaliDate(nepaliCreatedDate);
        int month = getMonthFromNepaliDate(nepaliCreatedDate);

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER)
                .setParameter(FROM_DATE, fetchStartingFiscalYear(year, month))
                .setParameter(TO_DATE, fetchEndingFiscalYear(year, month))
                .setParameter(HOSPITAL_ID, hospitalId);

        return AppointmentUtils.generateAppointmentNumber(query.getResultList());
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPOINTMENTS)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        List<AppointmentMinResponseDTO> pendingAppointments =
                transformQueryToResultList(query, AppointmentMinResponseDTO.class);

        if (pendingAppointments.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        }

        return pendingAppointments;
    }

    @Override
    public Double calculateRefundAmount(Long appointmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_AMOUNT)
                .setParameter(ID, appointmentId);

        try {
            return Double.parseDouble(query.getSingleResult().toString());
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
        }
    }

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
    public Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_REGISTERED_APPOINTMENT)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countFollowUpPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_FOLLOW_UP_APPOINTMENT)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_OVER_ALL_APPOINTMENTS)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAILS_BY_ID)
                .setParameter(ID, appointmentId);

        List<AppointmentDetailResponseDTO> appointmentDetails =
                transformQueryToResultList(query, AppointmentDetailResponseDTO.class);

        if (appointmentDetails.isEmpty()) throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);

        return appointmentDetails.get(0);
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_HISTORY)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        List<AppointmentMinResponseDTO> appointmentHistory =
                transformQueryToResultList(query, AppointmentMinResponseDTO.class);

        if (appointmentHistory.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        }

        return appointmentHistory;
    }

    @Override
    public AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                          Pageable pageable,
                                                                          Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_RESCHEDULE_APPOINTMENT_LOGS.apply(rescheduleDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(rescheduleDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(rescheduleDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentRescheduleLogResponseDTO results = parseQueryResultToAppointmentRescheduleLogResponse(objects);

        if (results.getAppointmentRescheduleLogDTOS().isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            results.setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                                Pageable pageable,
                                                                Long hospitalId) {

        Query query = getQueryToFetchRefundAppointments(searchDTO, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentRefundDTO> refundAppointments = transformQueryToResultList(query, AppointmentRefundDTO.class);

        if (refundAppointments.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            Double totalRefundAmount = calculateTotalRefundAmount(searchDTO, hospitalId);

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
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO,
            Pageable pageable,
            Long hospitalId) {

        AppointmentPendingApprovalResponseDTO appointmentPendingApprovalResponseDTO = new AppointmentPendingApprovalResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPROVALS.apply(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentPendingApprovalDTO> appointmentPendingApprovalDTOS =
                transformQueryToResultList(query, AppointmentPendingApprovalDTO.class);

        if (appointmentPendingApprovalDTOS.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
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
    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO,
                                                                                   Long hospitalId) {

        Query query = createNativeQuery.apply(entityManager,
                QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToAppointmentStatusResponseDTOS(results);
    }

    @Override
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable,
                                                           Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_LOGS.apply(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        if (objects.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            AppointmentLogResponseDTO results = parseQueryResultToAppointmentLogResponse(objects);
            results.setTotalItems(totalItems);
            calculateAppointmentStatistics(searchRequestDTO, results, hospitalId);
            return results;
        }
    }

    @Override
    public TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                           Pageable pageable,
                                                           Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TRANSACTION_LOGS.apply(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        TransactionLogResponseDTO results = parseQueryResultToTransactionLogResponse(objects);

        if (results.getTransactionLogs().isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            results.setTotalItems(totalItems);
            calculateAppointmentStatisticsForTransactionLog(searchRequestDTO, results, hospitalId);
            return results;
        }
    }

    @Override
    public List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              Long hospitalId,
                                                              Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_QUEUE
                .apply(appointmentQueueRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(DATE, utilDateToSqlDate(appointmentQueueRequestDTO.getDate()));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentQueueDTO> results = transformQueryToResultList(query, AppointmentQueueDTO.class);

        if (results.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Long hospitalId,
            Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_QUEUE.apply(appointmentQueueRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(DATE, utilDateToSqlDate(appointmentQueueRequestDTO.getDate()));

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        Map<String, List<AppointmentQueueDTO>> results =
                parseQueryResultToAppointmentQueueForTodayByTimeResponse(objects);

        return results;
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

    private Query getQueryToFetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                    Long hospitalId) {

        return createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_APPOINTMENTS(searchDTO))
                .setParameter(HOSPITAL_ID, hospitalId);
    }

    private Double calculateTotalRefundAmount(AppointmentRefundSearchDTO searchDTO,
                                              Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(searchDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Double) query.getSingleResult();
    }

    private void calculateAppointmentStatistics(AppointmentLogSearchDTO searchRequestDTO,
                                                AppointmentLogResponseDTO appointmentLogResponse,
                                                Long hospitalId) {

        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentDetails(searchRequestDTO, hospitalId);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInAppointmentDetails(searchRequestDTO, hospitalId);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentDetails(searchRequestDTO, hospitalId);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentDetails(searchRequestDTO, hospitalId);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentDetails(searchRequestDTO, hospitalId);

        parseToAppointmentLogResponseDTO(
                appointmentLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentAmount(searchRequestDTO, hospitalId, appointmentLogResponse);
    }

    private void calculateAppointmentStatisticsForTransactionLog(TransactionLogSearchDTO searchRequestDTO,
                                                                 TransactionLogResponseDTO txnLogResponse,
                                                                 Long hospitalId) {

        BookedAppointmentResponseDTO bookedInfo = getBookedAppointmentDetails(searchRequestDTO, hospitalId);

        CheckedInAppointmentResponseDTO checkedInInfo = getCheckedInAppointmentDetails(searchRequestDTO, hospitalId);

        CancelledAppointmentResponseDTO cancelledInfo = getCancelledAppointmentDetails(searchRequestDTO, hospitalId);

        RefundAppointmentResponseDTO refundInfo = getRefundedAppointmentDetails(searchRequestDTO, hospitalId);

        RevenueFromRefundAppointmentResponseDTO revenueFromRefund =
                getRevenueFromRefundedAppointmentDetails(searchRequestDTO, hospitalId);

        parseToTxnLogResponseDTO(
                txnLogResponse,
                bookedInfo,
                checkedInInfo,
                cancelledInfo,
                refundInfo,
                revenueFromRefund
        );

        calculateTotalAppointmentAmount(searchRequestDTO, hospitalId, txnLogResponse);
    }

    private BookedAppointmentResponseDTO getBookedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                     Long hospitalId) {

         /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> bookedResult = query.getResultList();

         /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                           Long hospitalId) {

         /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> checkInResult = query.getResultList();

         /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                           Long hospitalId) {

          /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_APPOINTMENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> cancelledResult = query.getResultList();

         /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                       Long hospitalId) {

         /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_APPOINTMENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundResult = query.getResultList();

         /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                                                             Long hospitalId) {


           /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundResult = query.getResultList();

         /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalAppointmentAmount(AppointmentLogSearchDTO searchRequestDTO,
                                                 Long hospitalId,
                                                 AppointmentLogResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_DETAILS_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException
                (AppointmentPendingApprovalDetailResponseDTO.class, "appointmentId", appointmentId.toString());
    };

    private void error() {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT);
    }

    private BookedAppointmentResponseDTO getBookedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                     Long hospitalId) {

         /*BOOKED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> bookedResult = query.getResultList();

         /*BOOKED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_BOOKED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> bookedWithFollowUpResult = query1.getResultList();

        return parseBookedAppointmentDetails(bookedResult.get(0), bookedWithFollowUpResult.get(0));
    }

    private CheckedInAppointmentResponseDTO getCheckedInAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                           Long hospitalId) {

        /*CHECK IN INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> checkInResult = query.getResultList();

         /*CHECK IN INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> checkInWithFollowUpResult = query1.getResultList();

        return parseCheckedInAppointmentDetails(checkInResult.get(0), checkInWithFollowUpResult.get(0));
    }

    private CancelledAppointmentResponseDTO getCancelledAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                           Long hospitalId) {

          /*CANCELLED INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> cancelledResult = query.getResultList();

         /*CANCELLED INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_CANCELLED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> cancelledWithFollowUpResult = query1.getResultList();

        return parseCancelledAppointmentDetails(cancelledResult.get(0), cancelledWithFollowUpResult.get(0));
    }

    private RefundAppointmentResponseDTO getRefundedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                       Long hospitalId) {
   /*REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundResult = query.getResultList();

         /*REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRefundedAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private RevenueFromRefundAppointmentResponseDTO getRevenueFromRefundedAppointmentDetails(TransactionLogSearchDTO searchRequestDTO,
                                                                                             Long hospitalId) {
        /*REVENUE FROM REFUND INFO WITHOUT FOLLOW UP*/
        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundResult = query.getResultList();

         /*REVENUE FROM REFUND INFO WITH FOLLOW UP*/
        Query query1 = createQuery.apply(entityManager,
                QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_WITH_FOLLOW_UP_FOR_TXN_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> refundWithFollowUpResult = query1.getResultList();

        return parseRevenueFromRefundAppointmentDetails(refundResult.get(0), refundWithFollowUpResult.get(0));
    }

    private void calculateTotalAppointmentAmount(TransactionLogSearchDTO searchRequestDTO,
                                                 Long hospitalId,
                                                 TransactionLogResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT_FOR_TRANSACTION_LOG(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }
}
