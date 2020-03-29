package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PARENT_APPOINTMENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentFollowUpTrackerRepositoryCustomImpl implements AppointmentFollowUpTrackerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AppointmentFollowUpTracker fetchAppointmentFollowUpTracker(Long patientId,
                                                                      Long doctorId,
                                                                      Long specializationId,
                                                                      Long hospitalId) {
        try {
            return entityManager.createQuery(
                    QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER, AppointmentFollowUpTracker.class)
                    .setParameter(PATIENT_ID, patientId)
                    .setParameter(DOCTOR_ID, doctorId)
                    .setParameter(SPECIALIZATION_ID, specializationId)
                    .setParameter(HOSPITAL_ID, hospitalId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public AppointmentFollowUpTracker fetchLatestAppointmentFollowUpTracker(Long parentAppointmentId) {
        try {
            return (AppointmentFollowUpTracker) entityManager.createNativeQuery(
                    QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER, AppointmentFollowUpTracker.class)
                    .setParameter(PARENT_APPOINTMENT_ID, parentAppointmentId)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new NoContentFoundException(AppointmentFollowUpTracker.class);
        }
    }

}
