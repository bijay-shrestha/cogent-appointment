package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentPendingSearchDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentPendingResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueSearchDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.client.utils.AppointmentUtils;
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
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.*;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.parseQueryResultToAppointmentQueueForTodayByTimeResponse;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.parseQueryResultToAppointmentQueueForTodayResponse;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

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

    /*USED WHILE SAVING DOCTOR DUTY ROSTER TO VALIDATE IF APPOINTMENT EXISTS*/
    @Override
    public List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(AppointmentCheckAvailabilityRequestDTO requestDTO) {

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
    public List<AppointmentPendingResponseDTO> fetchPendingAppointments(AppointmentPendingSearchDTO searchDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PENDING_APPOINTMENTS)
                .setParameter(FROM_DATE, utilDateToSqlDate(searchDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchDTO.getToDate()));

        return transformQueryToResultList(query, AppointmentPendingResponseDTO.class);
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
    public Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_REGISTERED_APPOINTMENT)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, dashBoardRequestDTO.getHospitalId());

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_COUNT_NEW_PATIENT_APPOINTMENT)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, dashBoardRequestDTO.getHospitalId());

        return (Long) query.getSingleResult();
    }

    @Override
    public Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_OVER_ALL_APPOINTMENTS)
                .setParameter(FROM_DATE, utilDateToSqlDate(dashBoardRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(dashBoardRequestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, dashBoardRequestDTO.getHospitalId());

        return (Long) query.getSingleResult();
    }


    @Override
    public AppointmentQueueSearchDTO fetchTodayAppointmentQueue(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                                Long hospitalId,
                                                                Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TODAY_APPOINTMENT_QUEUE.apply(appointmentQueueRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        AppointmentQueueSearchDTO results = parseQueryResultToAppointmentQueueForTodayResponse(objects);

        if (results.getAppointmentQueueByTimeDTOList().isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Long hospitalId,
            Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_TODAY_APPOINTMENT_QUEUE.apply(appointmentQueueRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<Object[]> objects = query.getResultList();

        Map<String, List<AppointmentQueueDTO>> results = parseQueryResultToAppointmentQueueForTodayByTimeResponse(objects);

        return results;
    }

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };
}
