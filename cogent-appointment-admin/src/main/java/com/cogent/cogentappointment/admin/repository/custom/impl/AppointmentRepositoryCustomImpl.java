package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.AppointmentQuery;
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
import static com.cogent.cogentappointment.admin.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.admin.query.AppointmentQuery.QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentRevenueStatisticsUtils.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.*;
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
    public AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                                Pageable pageable) {

        Query query = getQueryToFetchRefundAppointments(searchDTO);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentRefundDTO> refundAppointments = transformQueryToResultList(query, AppointmentRefundDTO.class);

        if (refundAppointments.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
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

        if (results.isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
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

    private Query getQueryToFetchRefundAppointments(AppointmentRefundSearchDTO searchDTO) {
        return createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_APPOINTMENTS(searchDTO));
    }

    private Double calculateTotalRefundAmount(AppointmentRefundSearchDTO searchDTO) {
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
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_LOGS.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentLogResponseDTO results = parseQueryResultToAppointmentLogResponse(objects);

        if (results.getAppointmentLogs().isEmpty()) {
            error();
            throw APPOINTMENT_NOT_FOUND.get();
        } else {
            results.setTotalItems(totalItems);
            results.setAppointmentStatistics(calculateAppointmentStatistics(searchRequestDTO));
            return results;
        }
    }

    @Override
    public AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                          Pageable pageable) {
        Query query = createQuery.apply(entityManager, AppointmentQuery.QUERY_TO_RESCHEDULE_APPOINTMENT_LOGS.apply(rescheduleDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(rescheduleDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(rescheduleDTO.getToDate()));

        int totalItems = query.getResultList().size();


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

    private AppointmentRevenueStatisticsResponseDTO calculateAppointmentStatistics(AppointmentLogSearchDTO searchRequestDTO) {

        AppointmentRevenueStatisticsResponseDTO responseDTO = new AppointmentRevenueStatisticsResponseDTO();

        getCheckedInAppointmentDetails(searchRequestDTO, responseDTO);

        getBookedAppointmentDetails(searchRequestDTO, responseDTO);

        getCancelledAppointmentDetails(searchRequestDTO, responseDTO);

        getRefundedAppointmentDetails(searchRequestDTO, responseDTO);

        getRevenueFromRefundedAppointmentDetails(searchRequestDTO, responseDTO);

        calculateTotalAppointmentAmount(searchRequestDTO, responseDTO);

        calculateTotalAmountExcludingBooked(searchRequestDTO, responseDTO);

        return responseDTO;
    }

    private void calculateTotalAppointmentAmount(AppointmentLogSearchDTO searchRequestDTO,
                                                 AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT(searchRequestDTO));

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmount(totalAppointmentAmount);
    }

    private void calculateTotalAmountExcludingBooked(AppointmentLogSearchDTO searchRequestDTO,
                                                     AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_APPOINTMENT_AMOUNT_EXCLUDING_BOOKED(searchRequestDTO));

        Double totalAppointmentAmount = (Double) query.getSingleResult();

        responseDTO.setTotalAmountExcludingBooked(totalAppointmentAmount);
    }

    private void getCheckedInAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CHECKED_IN_APPOINTMENT_AMOUNT(searchRequestDTO));

        List<Object[]> results = query.getResultList();

        parseCheckedInAppointmentDetails(results.get(0), responseDTO);
    }

    private void getBookedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                             AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT_AMOUNT(searchRequestDTO));

        List<Object[]> results = query.getResultList();


        parseBookedAppointmentDetails(results.get(0), responseDTO);
    }

    private void getCancelledAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                                AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CANCELLED_APPOINTMENT_AMOUNT(searchRequestDTO));

        List<Object[]> results = query.getResultList();

        parseCancelledAppointmentDetails(results.get(0), responseDTO);
    }

    private void getRefundedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,
                                               AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REFUNDED_APPOINTMENT_AMOUNT(searchRequestDTO));

        List<Object[]> results = query.getResultList();

        parseRefundedAppointmentDetails(results.get(0), responseDTO);
    }

    private void getRevenueFromRefundedAppointmentDetails(AppointmentLogSearchDTO searchRequestDTO,

                                                          AppointmentRevenueStatisticsResponseDTO responseDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_REVENUE_REFUNDED_APPOINTMENT_AMOUNT(searchRequestDTO));

        List<Object[]> results = query.getResultList();

        parseRevenueFromRefundedAppointmentDetails(results.get(0), responseDTO);
    }


    private Function<Long, NoContentFoundException> APPOINTMENT_DETAILS_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException
                (AppointmentPendingApprovalDetailResponseDTO.class, "appointmentId", appointmentId.toString());
    };

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT);
    }
}
