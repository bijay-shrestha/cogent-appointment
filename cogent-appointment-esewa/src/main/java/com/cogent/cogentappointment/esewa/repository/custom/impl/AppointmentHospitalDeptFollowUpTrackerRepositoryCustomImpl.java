package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentHospitalDeptFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.*;
import static com.cogent.cogentappointment.esewa.query.AppointmentHospitalDeptFollowUpTrackerQuery.QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentHospitalDeptFollowUpTrackerRepositoryCustomImpl implements
        AppointmentHospitalDeptFollowUpTrackerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AppointmentHospitalDepartmentFollowUpTracker fetchAppointmentHospitalDeptFollowUpTracker(
            Long hospitalId, Long hospitalDepartmentId, Long patientId) {

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER,
                    AppointmentHospitalDepartmentFollowUpTracker.class)
                    .setParameter(HOSPITAL_ID, hospitalId)
                    .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                    .setParameter(PATIENT_ID, patientId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

}
