package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;
import com.cogent.cogentappointment.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.DoctorDutyRoster;
import com.cogent.cogentappointment.repository.custom.DoctorDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.utils.DoctorDutyRosterUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.constants.QueryConstants.*;
import static com.cogent.cogentappointment.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.query.DoctorDutyRosterOverrideQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_DETAILS;
import static com.cogent.cogentappointment.query.DoctorDutyRosterQuery.*;
import static com.cogent.cogentappointment.utils.DoctorDutyRosterUtils.parseToDoctorDutyRosterDetailResponseDTO;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDayCodeFromDate;
import static com.cogent.cogentappointment.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.*;

/**
 * @author smriti on 26/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class DoctorDutyRosterRepositoryCustomImpl implements DoctorDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDoctorDutyRosterCount(Long doctorId,
                                              Long specializationId,
                                              Date fromDate,
                                              Date toDate) {

        Query query = createQuery.apply(entityManager, VALIDATE_DOCTOR_DUTY_ROSTER_COUNT)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR_DUTY_ROSTER(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DoctorDutyRosterMinimalResponseDTO> results = transformQueryToResultList(
                query, DoctorDutyRosterMinimalResponseDTO.class);

        if (results.isEmpty()) throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long doctorDutyRosterId) {

        DoctorDutyRosterResponseDTO doctorDutyRosterResponseDTO =
                fetchDoctorDutyRosterDetails(doctorDutyRosterId);

        List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysDutyRosterResponseDTO =
                fetchDoctorWeekDaysDutyRosterResponseDTO(doctorDutyRosterId);

        List<DoctorDutyRosterOverrideResponseDTO> overrideResponseDTOS =
                doctorDutyRosterResponseDTO.getHasOverrideDutyRoster().equals(YES)
                        ? fetchDoctorDutyRosterOverrideResponseDTO(doctorDutyRosterId) : new ArrayList<>();

        return parseToDoctorDutyRosterDetailResponseDTO(
                doctorDutyRosterResponseDTO, weekDaysDutyRosterResponseDTO, overrideResponseDTOS);
    }

    @Override
    public DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(Date date, Long doctorId, Long specializationId) {

        Date sqlDate = utilDateToSqlDate(date);

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_TIME)
                .setParameter(DATE, sqlDate)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(CODE, getDayCodeFromDate(sqlDate));

        try {
            return transformQueryToSingleResult(query, DoctorDutyRosterTimeResponseDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(
            DoctorDutyRosterStatusRequestDTO requestDTO) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return DoctorDutyRosterUtils.parseQueryResultToDoctorDutyRosterStatusResponseDTOS(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }

    private DoctorDutyRosterResponseDTO fetchDoctorDutyRosterDetails(Long doctorDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_DETAILS)
                .setParameter(ID, doctorDutyRosterId);
        try {
            return transformQueryToSingleResult(query, DoctorDutyRosterResponseDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(doctorDutyRosterId);
        }
    }

    private List<DoctorWeekDaysDutyRosterResponseDTO> fetchDoctorWeekDaysDutyRosterResponseDTO(
            Long doctorDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_WEEK_DAYS_DUTY_ROSTER)
                .setParameter(ID, doctorDutyRosterId);

        return transformQueryToResultList(query, DoctorWeekDaysDutyRosterResponseDTO.class);
    }

    private List<DoctorDutyRosterOverrideResponseDTO> fetchDoctorDutyRosterOverrideResponseDTO(
            Long doctorDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_DETAILS)
                .setParameter(ID, doctorDutyRosterId);

        return transformQueryToResultList(query, DoctorDutyRosterOverrideResponseDTO.class);
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () ->
            new NoContentFoundException(DoctorDutyRoster.class);

    private Function<Long, NoContentFoundException> DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND =
            (doctorDutyRosterId) -> {
                throw new NoContentFoundException
                        (DoctorDutyRoster.class, "doctorDutyRosterId", doctorDutyRosterId.toString());
            };
}
