package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DoctorDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.DoctorDutyRosterOverrideQuery.*;
import static com.cogent.cogentappointment.admin.utils.DoctorDutyRosterOverrideUtils.parseQueryResultToDoctorDutyRosterStatusResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

@Repository
@Transactional(readOnly = true)
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
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus(
            DoctorDutyRosterStatusRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToDoctorDutyRosterStatusResponseDTO(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }

    @Override
    public List<DoctorDutyRosterOverride> fetchDoctorDutyRosterOverrides(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS) {

        List<DoctorDutyRosterOverride> doctorDutyRosterOverrides =
                entityManager.createQuery(QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE(updateRequestDTOS),
                        DoctorDutyRosterOverride.class)
                        .getResultList();

        if(doctorDutyRosterOverrides.isEmpty())
            throw DOCTOR_DUTY_ROSTER_OVERRIDE_NOT_FOUND.get();

        return doctorDutyRosterOverrides;
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_OVERRIDE_NOT_FOUND = () ->
            new NoContentFoundException(DoctorDutyRosterOverride.class);

}
