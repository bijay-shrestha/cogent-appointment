package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentResponseDTO;
import com.cogent.cogentappointment.admin.model.Appointment;
import com.cogent.cogentappointment.admin.query.AppointmentQuery;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.AppointmentUtils;
import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentBookedDateResponseDTO;
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

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentRepositoryCustomImpl implements AppointmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AppointmentBookedTimeResponseDTO> checkAvailability(AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Query query = QueryUtils.createQuery.apply(entityManager, AppointmentQuery.QUERY_TO_FETCH_BOOKED_APPOINTMENT)
                .setParameter(QueryConstants.DATE, DateUtils.utilDateToSqlDate(requestDTO.getAppointmentDate()))
                .setParameter(QueryConstants.DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(QueryConstants.SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return QueryUtils.transformQueryToResultList(query, AppointmentBookedTimeResponseDTO.class);
    }

    @Override
    public String generateAppointmentNumber(String nepaliCreatedDate) {

        int year = DateUtils.getYearFromNepaliDate(nepaliCreatedDate);
        int month = DateUtils.getMonthFromNepaliDate(nepaliCreatedDate);

        Query query = QueryUtils.createNativeQuery.apply(entityManager, AppointmentQuery.QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER)
                .setParameter(QueryConstants.FROM_DATE, DateUtils.fetchStartingFiscalYear(year, month))
                .setParameter(QueryConstants.TO_DATE, DateUtils.fetchEndingFiscalYear(year, month));

        return AppointmentUtils.generateAppointmentNumber(query.getResultList());
    }

    @Override
    public List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                              Date toDate,
                                                                              Long doctorId,
                                                                              Long specializationId) {

        Query query = QueryUtils.createQuery.apply(entityManager, AppointmentQuery.QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES)
                .setParameter(QueryConstants.FROM_DATE, DateUtils.utilDateToSqlDate(fromDate))
                .setParameter(QueryConstants.TO_DATE, DateUtils.utilDateToSqlDate(toDate))
                .setParameter(QueryConstants.DOCTOR_ID, doctorId)
                .setParameter(QueryConstants.SPECIALIZATION_ID, specializationId);

        return QueryUtils.transformQueryToResultList(query, AppointmentBookedDateResponseDTO.class);
    }

    @Override
    public Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId) {

        Query query = QueryUtils.createQuery.apply(entityManager, AppointmentQuery.QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT)
                .setParameter(QueryConstants.FROM_DATE, DateUtils.utilDateToSqlDate(fromDate))
                .setParameter(QueryConstants.TO_DATE, DateUtils.utilDateToSqlDate(toDate))
                .setParameter(QueryConstants.DOCTOR_ID, doctorId)
                .setParameter(QueryConstants.SPECIALIZATION_ID, specializationId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable) {

        Query query = QueryUtils.createQuery.apply(entityManager, AppointmentQuery.QUERY_TO_SEARCH_APPOINTMENT.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<AppointmentMinimalResponseDTO> results = QueryUtils.transformQueryToResultList(
                query, AppointmentMinimalResponseDTO.class);

        if (results.isEmpty()) throw APPOINTMENT_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public AppointmentResponseDTO fetchDetailsById(Long id) {
        Query query = QueryUtils.createQuery.apply(entityManager, AppointmentQuery.QUERY_TO_FETCH_APPOINTMENT_DETAILS)
                .setParameter(QueryConstants.ID, id);
        try {
            return QueryUtils.transformQueryToSingleResult(query, AppointmentResponseDTO.class);
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
