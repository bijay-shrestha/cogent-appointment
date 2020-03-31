package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.query.EsewaQuery;
import com.cogent.cogentappointment.client.repository.custom.DoctorDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
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
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER_OVERRIDE;
import static com.cogent.cogentappointment.client.query.DoctorDutyRosterOverrideQuery.*;
import static com.cogent.cogentappointment.client.query.DoctorDutyRosterOverrideQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS;
import static com.cogent.cogentappointment.client.query.EsewaQuery.*;
import static com.cogent.cogentappointment.client.query.EsewaQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS;
import static com.cogent.cogentappointment.client.utils.DoctorDutyRosterOverrideUtils.parseQueryResultToDoctorDutyRosterStatusResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorDutyRosterOverrideRepositoryCustomImpl implements DoctorDutyRosterOverrideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchOverrideCount(Long doctorId, Long specializationId,
                                   Date fromDate, Date toDate) {

        Query query = createQuery.apply(entityManager, VALIDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_COUNT)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchOverrideCount(Long doctorDutyRosterOverrideId, Long doctorId,
                                   Long specializationId, Date fromDate, Date toDate) {

        Query query = createQuery.apply(entityManager, VALIDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_COUNT_FOR_UPDATE)
                .setParameter(ID, doctorDutyRosterOverrideId)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(Date date,
                                                                             Long doctorId,
                                                                             Long specializationId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_TIME)
                .setParameter(DATE, utilDateToSqlDate(date))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDutyRosterTimeResponseDTO> responseDTOList =
                transformQueryToResultList(query, DoctorDutyRosterTimeResponseDTO.class);

        return responseDTOList.isEmpty() ? null : responseDTOList.get(0);
    }

    @Override
    public List<DoctorDutyRosterOverride> fetchDoctorDutyRosterOverrides(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS) {

        List<DoctorDutyRosterOverride> doctorDutyRosterOverrides =
                entityManager.createQuery(QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE(updateRequestDTOS),
                        DoctorDutyRosterOverride.class)
                        .getResultList();

        if (doctorDutyRosterOverrides.isEmpty()){
            error();
            throw DOCTOR_DUTY_ROSTER_OVERRIDE_NOT_FOUND.get();
        }

        return doctorDutyRosterOverrides;
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus(
            AppointmentStatusRequestDTO requestDTO,
            Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToDoctorDutyRosterStatusResponseDTO(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }

    @Override
    public List<DoctorDutyRosterOverrideAppointmentDate> getRosterOverrideByRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DUTY_ROSTER_OVERRIDE_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        try {
            return transformQueryToResultList(query, DoctorDutyRosterOverrideAppointmentDate.class);
        } catch (NoResultException e) {
            error();
            throw new NoContentFoundException("Not Found");
        }
    }

    @Override
    public List<DoctorDutyRosterOverrideAppointmentDate> getRosterOverrideByDoctorAndSpecializationId(Long doctorId,
                                                                                                      Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DUTY_ROSTER_OVERRIDE_BY_DOCTOR_AND_SPECIALIZATION_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        try {
            return transformQueryToResultList(query, DoctorDutyRosterOverrideAppointmentDate.class);
        } catch (NoResultException e) {
            error();
            throw new NoContentFoundException("Not Found");
        }
    }

    @Override
    public DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterOverrideStatus(AppointmentDetailRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(requestDTO))
                .setParameter(DATE, requestDTO.getDate())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToSingleResult(query, DoctorAvailabilityStatusResponseDTO.class);
    }

    @Override
    public List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_AVAILABLE_DOCTORS_FROM_DDR_OVERRIDE(requestDTO))
                .setParameter(DATE, requestDTO.getDate())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        return transformQueryToResultList(query, AvailableDoctorWithSpecialization.class);
    }

    @Override
    public List<DutyRosterOverrideAppointmentDate> fetchDayOffRosterOverridebyRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager,
                EsewaQuery.QUERY_TO_FETCH_DAY_OFF_ROSTER_OVERRIDE_DATES)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        return transformQueryToResultList(query, DutyRosterOverrideAppointmentDate.class);
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_OVERRIDE_NOT_FOUND = () ->
            new NoContentFoundException(DoctorDutyRosterOverride.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER_OVERRIDE);
    }

}
