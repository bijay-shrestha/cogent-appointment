package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.dto.request.appointment.AppointmentSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentTimeResponseDTO;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Appointment;
import com.cogent.cogentappointment.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.utils.AppointmentUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.constants.QueryConstants.*;
import static com.cogent.cogentappointment.query.AppointmentQuery.*;
import static com.cogent.cogentappointment.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.*;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AppointmentTimeResponseDTO> checkAvailability(Date date, Long doctorId,
                                                              Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BOOKED_APPOINTMENT)
                .setParameter(DATE, utilDateToSqlDate(date))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        return transformQueryToResultList(query, AppointmentTimeResponseDTO.class);
    }

    @Override
    public String generateAppointmentNumber(String nepaliCreatedDate) {

        int year = getYearFromNepaliDate(nepaliCreatedDate);
        int month = getMonthFromNepaliDate(nepaliCreatedDate);

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER)
                .setParameter(FROM_DATE, fetchStartingFiscalYear(year, month))
                .setParameter(TO_DATE, fetchEndingFiscalYear(year, month));

        return AppointmentUtils.generateAppointmentNumber(query.getResultList());
    }

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
    public List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_APPOINTMENT.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AppointmentMinimalResponseDTO> results = transformQueryToResultList(
                query, AppointmentMinimalResponseDTO.class);

        if (results.isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_DETAILS)
                .setParameter(ID, id);
        try {
            return transformQueryToSingleResult(query, AppointmentResponseDTO.class);
        } catch (NoResultException e) {
            throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(id);
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
//        return parseQueryResultToAppointmentStatusResponseDTOS(results);
//    }


    private Supplier<NoContentFoundException> APPOINTMENT_NOT_FOUND = ()
            -> new NoContentFoundException(Appointment.class);

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };
}
