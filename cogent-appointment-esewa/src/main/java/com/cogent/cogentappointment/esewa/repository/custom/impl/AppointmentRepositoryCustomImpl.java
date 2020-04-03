package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.esewa.utils.AppointmentUtils;
import com.cogent.cogentappointment.persistence.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.AppointmentConstants.APPOINTMENT_TIME;
import static com.cogent.cogentappointment.esewa.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;


/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
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

        if (pendingAppointments.isEmpty())
            throw new NoContentFoundException(Appointment.class);

        return pendingAppointments;
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

        if (appointmentHistory.isEmpty())
            throw new NoContentFoundException(Appointment.class);

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

        if (results.getAppointmentRescheduleLogDTOS().isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                                                             Pageable pageable,
                                                                             Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPROVALS.apply(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(HOSPITAL_ID, hospitalId);

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
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentLogResponseDTO results = parseQueryResultToAppointmentLogResponse(objects);

        if (results.getAppointmentLogs().isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.setTotalItems(totalItems);
            return results;
        }
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


    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

}
