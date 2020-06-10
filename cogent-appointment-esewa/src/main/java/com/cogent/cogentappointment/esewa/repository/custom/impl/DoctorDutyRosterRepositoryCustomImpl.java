package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AvailableDoctorRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterAppointmentDateAndDoctorDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterAppointmentDateAndSpecilizationDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.DoctorDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.esewa.query.DoctorDutyRosterQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_TIME;
import static com.cogent.cogentappointment.esewa.query.EsewaQuery.*;
import static com.cogent.cogentappointment.esewa.utils.AppointmentDetailsUtils.parseDoctorAvailabilityResponseStatus;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.conditionOfBothDateProvided;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDayCodeFromDate;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;

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
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND();
        }
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
    public List<DutyRosterAppointmentDateAndSpecilizationDTO> getAvailableDatesAndSpecilizationByDoctorId(
            Long doctorId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVAILABLE_DATES_WITH_SPECIALIZATION)
                .setParameter(DOCTOR_ID, doctorId);

        return transformQueryToResultList(query, DutyRosterAppointmentDateAndSpecilizationDTO.class);
    }

    @Override
    public List<DutyRosterAppointmentDateAndDoctorDTO> getAvailableDatesAndDoctorBySpecilizationId(Long specilizationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_AVALIABLE_DATES_WITH_DOCTOR)
                .setParameter(SPECIALIZATION_ID, specilizationId);

        return transformQueryToResultList(query, DutyRosterAppointmentDateAndDoctorDTO.class);
    }

    private NoContentFoundException DOCTOR_DUTY_ROSTER_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
        throw new NoContentFoundException(DoctorDutyRoster.class);
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
}
