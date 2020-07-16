package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalFollowUpResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.client.query.HospitalQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer fetchHospitalFollowUpIntervalDays(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public Integer fetchHospitalFollowUpCount(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public HospitalFollowUpResponseDTO fetchFollowUpDetails(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_DETAILS)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, HospitalFollowUpResponseDTO.class);
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, HOSPITAL);
            throw HOSPITAL_NOT_FOUND.get();
        }
    }

    @Override
    public List<AppointmentServiceTypeDropDownResponseDTO> fetchAssignedAppointmentServiceType(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<AppointmentServiceTypeDropDownResponseDTO> appointmentServiceTypes =
                transformQueryToResultList(query, AppointmentServiceTypeDropDownResponseDTO.class);

        return appointmentServiceTypes.isEmpty() ? new ArrayList<>() : appointmentServiceTypes;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> new NoContentFoundException(Hospital.class);
}



