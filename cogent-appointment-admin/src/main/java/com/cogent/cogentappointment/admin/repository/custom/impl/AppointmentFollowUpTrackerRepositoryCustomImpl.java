package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PARENT_APPOINTMENT_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PATIENT_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.DOCTOR_ID;
import static com.cogent.cogentappointment.admin.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.admin.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_LATEST_APPOINTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class AppointmentFollowUpTrackerRepositoryCustomImpl implements AppointmentFollowUpTrackerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public List<FollowUpTrackerResponseDTO> fetchMinimalFollowUpTracker(FollowUpTrackerSearchRequestDTO requestDTO) {
//        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MINIMAL_FOLLOW_UP_TRACKER)
//                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
//                .setParameter(PATIENT_ID, requestDTO.getPatientId());
//
//        return transformQueryToResultList(query, FollowUpTrackerResponseDTO.class);
//    }

    @Override
    public AppointmentFollowUpTracker fetchAppointmentFollowUpTracker(String parentAppointmentNumber,
                                                                      Long doctorId,
                                                                      Long patientId,
                                                                      Long specializationId) {

        return entityManager.createQuery(
                QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER, AppointmentFollowUpTracker.class)
                .setParameter("", parentAppointmentNumber)
                .setParameter(PATIENT_ID, patientId)
                .setParameter(DOCTOR_ID, doctorId)
                .getSingleResult();
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
