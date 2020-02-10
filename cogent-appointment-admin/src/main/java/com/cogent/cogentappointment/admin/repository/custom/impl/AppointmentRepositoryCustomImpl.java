package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseQueryResultToAppointmentApprovalResponse;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseQueryResultToAppointmentLogResponse;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
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

        if (refundAppointments.isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            Double totalRefundAmount = calculateTotalRefundAmount(searchDTO);
            return AppointmentRefundResponseDTO.builder()
                    .refundAppointments(refundAppointments)
                    .totalItems(totalItems)
                    .totalRefundAmount(totalRefundAmount)
                    .build();
        }
    }

    private Query getQueryToFetchRefundAppointments(AppointmentRefundSearchDTO searchDTO) {
        return createQuery.apply(entityManager, QUERY_TO_FETCH_REFUND_APPOINTMENTS(searchDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));
    }

    private Double calculateTotalRefundAmount(AppointmentRefundSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TOTAL_REFUND_AMOUNT(searchDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        return (Double) query.getSingleResult();
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPROVALS.apply(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentPendingApprovalResponseDTO results = parseQueryResultToAppointmentApprovalResponse(objects);

        if (results.getPendingAppointmentApprovals().isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_LOGS.apply(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentLogResponseDTO results = parseQueryResultToAppointmentLogResponse(objects);

        if (results.getAppointmentLogSearchDTOList().isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.setTotalItems(totalItems);
            return results;
        }
    }

//    @Override
//    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(AppointmentStatusRequestDTO requestDTO) {
//
//        Query query = createNativeQuery.apply(entityManager,
//                QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(requestDTO))
//                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
//                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));
//
//        if (!Objects.isNull(requestDTO.getDoctorId()))
//            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());
//
//        if (!Objects.isNull(requestDTO.getSpecializationId()))
//            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());
//
//        List<Object[]> results = query.getResultList();
//
//        return parseQueryResultToAppointmentApprovalResponse(results);
//    }

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };
}
