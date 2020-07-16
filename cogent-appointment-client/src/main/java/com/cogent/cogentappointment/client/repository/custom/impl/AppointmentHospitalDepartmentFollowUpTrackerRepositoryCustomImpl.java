package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentFollowUpTrackerConstants.PARENT_APPOINTMENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PatientQueryConstants.PATIENT_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.query.AppointmentHospitalDepartmentFollowUpTrackerQuery.QUERY_TO_FETCH_LATEST_APPOINTMENT_HOSPITAL_DEPT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.query.AppointmentHospitalDepartmentFollowUpTrackerQuery.QUERY_TO_UPDATE_HOSPITAL_DEPT_APPOINTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 18/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustomImpl
        implements AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AppointmentHospitalDepartmentFollowUpTracker fetchLatestAppointmentFollowUpTracker(
            Long parentAppointmentId) {

        try {
            return (AppointmentHospitalDepartmentFollowUpTracker) entityManager.createNativeQuery(
                    QUERY_TO_FETCH_LATEST_APPOINTMENT_HOSPITAL_DEPT_FOLLOW_UP_TRACKER,
                    AppointmentHospitalDepartmentFollowUpTracker.class)
                    .setParameter(PARENT_APPOINTMENT_ID, parentAppointmentId)
                    .getSingleResult();

        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT_HOSPITAL_DEPARTMENT_FOLLOW_UP_TRACKER);
            throw new NoContentFoundException(AppointmentHospitalDepartmentFollowUpTracker.class);
        }
    }

    @Override
    public void updateAppointmentFollowUpTrackerStatus(Long hospitalId,
                                                       Long hospitalDepartmentId,
                                                       Long patientId) {

        Query query = entityManager.createQuery(QUERY_TO_UPDATE_HOSPITAL_DEPT_APPOINTMENT_FOLLOW_UP_TRACKER)
                .setParameter(PATIENT_ID, patientId)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(HOSPITAL_ID, hospitalId);

        query.executeUpdate();
    }

}
