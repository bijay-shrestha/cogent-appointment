package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PARENT_APPOINTMENT_NUMBER;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PATIENT_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.DOCTOR_ID;
import static com.cogent.cogentappointment.admin.query.AppointmentFollowUpTrackerQuery.QUERY_TO_FETCH_APPOINTMENT_FOLLOW_UP_TRACKER;

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
                .setParameter(PARENT_APPOINTMENT_NUMBER, parentAppointmentNumber)
                .setParameter(PATIENT_ID, patientId)
                .setParameter(DOCTOR_ID, doctorId)
                .getSingleResult();
    }
}
