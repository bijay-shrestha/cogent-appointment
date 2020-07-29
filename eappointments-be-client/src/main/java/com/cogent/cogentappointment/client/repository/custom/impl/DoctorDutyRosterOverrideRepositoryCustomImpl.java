package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.RosterDetailsForStatus;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
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
import static com.cogent.cogentappointment.client.utils.DoctorDutyRosterOverrideUtils.parseQueryResultToDoctorDutyRosterStatusResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createNativeQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

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
    public List<DoctorDutyRosterOverride> fetchDoctorDutyRosterOverrides(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS) {

        List<DoctorDutyRosterOverride> doctorDutyRosterOverrides =
                entityManager.createQuery(QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE(updateRequestDTOS),
                        DoctorDutyRosterOverride.class)
                        .getResultList();

        if (doctorDutyRosterOverrides.isEmpty()) {
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
    public RosterDetailsForStatus fetchOverrideRosterDetails(RosterDetailsForStatus rosterDetailsForStatus,
                                                             Date appointmentDate) {
        Query query =createNativeQuery.apply(entityManager,QUERY_TO_GET_OVERRIDE_TIME_BY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID,rosterDetailsForStatus.getRosterId())
                .setParameter(DATE,utilDateToSqlDate(appointmentDate));

        try{
            Object[] result= (Object[]) query.getSingleResult();
            rosterDetailsForStatus.setStartTime(result[0].toString());
            rosterDetailsForStatus.setEndTime(result[1].toString());
            return rosterDetailsForStatus;
        }catch (NoResultException e){
            return rosterDetailsForStatus;
        }
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_OVERRIDE_NOT_FOUND = () ->
            new NoContentFoundException(DoctorDutyRosterOverride.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER_OVERRIDE);
    }

}
