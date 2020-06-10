package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AvailableDoctorRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DutyRosterAppointmentDateAndDoctorDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DutyRosterAppointmentDateAndSpecilizationDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.DoctorDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
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

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.query.DoctorDutyRosterOverrideQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_DETAILS;
import static com.cogent.cogentappointment.client.query.DoctorDutyRosterQuery.*;
import static com.cogent.cogentappointment.client.query.DoctorDutyRosterQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS;
import static com.cogent.cogentappointment.client.query.EsewaQuery.*;
import static com.cogent.cogentappointment.client.query.EsewaQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS;
import static com.cogent.cogentappointment.client.utils.DoctorDutyRosterUtils.*;
import static com.cogent.cogentappointment.client.utils.EsewaUtils.parseDoctorAvailabilityResponseStatus;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 26/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
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
                                                           Pageable pageable,
                                                           Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR_DUTY_ROSTER(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DoctorDutyRosterMinimalResponseDTO> results = transformQueryToResultList(
                query, DoctorDutyRosterMinimalResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        } else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long doctorDutyRosterId, Long hospitalId) {

        DoctorDutyRosterResponseDTO doctorDutyRosterResponseDTO =
                fetchDoctorDutyRosterDetails(doctorDutyRosterId, hospitalId);

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
            error();
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }
    }

    @Override
    public List<DoctorExistingDutyRosterResponseDTO> fetchExistingDoctorDutyRosters(
            DoctorExistingDutyRosterRequestDTO requestDTO,
            Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_DOCTOR_DUTY_ROSTERS)
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId())
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        return transformQueryToResultList(query, DoctorExistingDutyRosterResponseDTO.class);
    }

    @Override
    public DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long doctorDutyRosterId) {
        Character hasOverrideRosters = checkIfDoctorDutyRosterOverrideExists(doctorDutyRosterId);

        List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysDutyRosterResponseDTO =
                fetchDoctorWeekDaysDutyRosterResponseDTO(doctorDutyRosterId);

        List<DoctorDutyRosterOverrideResponseDTO> overrideResponseDTOS =
                hasOverrideRosters.equals(YES)
                        ? fetchDoctorDutyRosterOverrideResponseDTO(doctorDutyRosterId)
                        : new ArrayList<>();

        return parseToExistingRosterDetails(weekDaysDutyRosterResponseDTO, overrideResponseDTOS);
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(
            AppointmentStatusRequestDTO requestDTO,
            Long hospitalId) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToDoctorDutyRosterStatusResponseDTOS(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }

    @Override
    public List<DoctorDutyRosterAppointmentDate> getDutyRosterByDoctorAndSpecializationId
            (AppointmentDatesRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DUTY_ROSTER_BY_DOCTOR_AND_SPECIALIZATION_ID)
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, DoctorDutyRosterAppointmentDate.class);
    }

    @Override
    public List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekDaysDutyRosterDataByDutyRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_WEEKDAYS_DUTY_ROSTER_DATA_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        List<DoctorWeekDaysDutyRosterAppointmentDate> responseDTOList = transformQueryToResultList(query,
                DoctorWeekDaysDutyRosterAppointmentDate.class);

        return responseDTOList;
    }

    @Override
    public List<String> getWeekDaysDutyRosterByDutyRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_WEEKDAYS_DUTY_ROSTER_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        return query.getResultList();
    }

    @Override
    public DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterStatus(AppointmentDetailRequestDTO requestDTO) {
        Date sqlDate = utilDateToSqlDate(requestDTO.getDate());

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS(requestDTO))
                .setParameter(DATE, utilDateToSqlDate(sqlDate))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId())
                .setParameter(CODE, getDayCodeFromDate(sqlDate));

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        DoctorAvailabilityStatusResponseDTO doctorAvailabilityStatus =
                transformQueryToSingleResult(query, DoctorAvailabilityStatusResponseDTO.class);

        parseDoctorAvailabilityResponseStatus(doctorAvailabilityStatus);

        return doctorAvailabilityStatus;
    }

    @Override
    public List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO) {
        Date sqlDate = utilDateToSqlDate(requestDTO.getDate());

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR(requestDTO))
                .setParameter(DATE, utilDateToSqlDate(sqlDate))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId())
                .setParameter(CODE, getDayCodeFromDate(sqlDate));

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AvailableDoctorWithSpecialization.class);
    }

    @Override
    public List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AvailableDoctorRequestDTO requestDTO) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR(requestDTO))
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (conditionOfBothDateProvided(requestDTO.getFromDate(), requestDTO.getToDate())) {
            query.setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()));
            query.setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));
        }

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        return transformNativeQueryToResultList(query, AvailableDoctorWithSpecialization.class);
    }

    @Override
    public List<DutyRosterAppointmentDateAndSpecilizationDTO> getAvaliableDatesAndSpecilizationByDoctorId(
            Long doctorId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVALIABLE_DATES_WITH_SPECILIZATION)
                .setParameter(DOCTOR_ID, doctorId);

        return transformQueryToResultList(query, DutyRosterAppointmentDateAndSpecilizationDTO.class);
    }

    @Override
    public List<DutyRosterAppointmentDateAndDoctorDTO> getAvaliableDatesAndDoctorBySpecilizationId(Long specilizationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVALIABLE_DATES_WITH_DOCTOR)
                .setParameter(SPECIALIZATION_ID, specilizationId);

        return transformQueryToResultList(query, DutyRosterAppointmentDateAndDoctorDTO.class);
    }

    private DoctorDutyRosterResponseDTO fetchDoctorDutyRosterDetails(Long doctorDutyRosterId, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_DETAILS)
                .setParameter(ID, doctorDutyRosterId)
                .setParameter(HOSPITAL_ID, hospitalId);
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
                log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR_DUTY_ROSTER, doctorDutyRosterId);
                throw new NoContentFoundException
                        (DoctorDutyRoster.class, "doctorDutyRosterId", doctorDutyRosterId.toString());
            };

    private Character checkIfDoctorDutyRosterOverrideExists(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_CHECK_IF_OVERRIDE_EXISTS)
                .setParameter(ID, doctorDutyRosterId);

        return (Character) query.getSingleResult();
    }

    private void error() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
    }
}
