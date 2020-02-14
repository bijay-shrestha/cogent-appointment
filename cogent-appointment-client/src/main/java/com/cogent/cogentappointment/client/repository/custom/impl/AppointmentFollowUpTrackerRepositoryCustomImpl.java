package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.AppointmentFollowUpTrackerQuery.QUERY_TO_VALIDATE_FOLLOW_UP_TRACKER_EXISTS;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentFollowUpTrackerRepositoryCustomImpl implements AppointmentFollowUpTrackerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateIfFollowUpTrackerExists(Long patientId,
                                                Long doctorId,
                                                Long specializationId,
                                                Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_FOLLOW_UP_TRACKER_EXISTS)
                .setParameter(PATIENT_ID, patientId)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        return results.isEmpty()? null: Long.parseLong(Arrays.toString(results.get(0)));
    }

}
