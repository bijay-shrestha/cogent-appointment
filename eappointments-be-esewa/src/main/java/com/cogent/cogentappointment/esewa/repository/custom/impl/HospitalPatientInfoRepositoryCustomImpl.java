package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalPatientInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.PATIENT_ID;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.query.HospitalPatientInfoQuery.QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO;
import static com.cogent.cogentappointment.esewa.query.HospitalPatientInfoQuery.QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO_COUNT;
import static com.cogent.cogentappointment.esewa.query.HospitalPatientInfoQuery.QUERY_TO_FETCH_PATIENT_REGISTERED_STATUS;

/**
 * @author smriti on 28/02/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalPatientInfoRepositoryCustomImpl implements HospitalPatientInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchHospitalPatientInfoCount(Long patientId, Long hospitalId) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO_COUNT)
                .setParameter(PATIENT_ID, patientId)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public HospitalPatientInfo fetchHospitalPatientInfo(Long patientId, Long hospitalId) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO, HospitalPatientInfo.class)
                    .setParameter(PATIENT_ID, patientId)
                    .setParameter(HOSPITAL_ID, hospitalId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Character fetchPatientRegisteredStatus(Long patientId, Long hospitalId) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_PATIENT_REGISTERED_STATUS)
                .setParameter(PATIENT_ID, patientId)
                .setParameter(HOSPITAL_ID, hospitalId);
        try {
            return (Character) query.getSingleResult();
        } catch (NoResultException ex) {
            return NO;
        }
    }
}
