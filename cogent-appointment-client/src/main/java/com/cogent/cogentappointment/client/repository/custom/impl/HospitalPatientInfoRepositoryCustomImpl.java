package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.HospitalPatientInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.PATIENT_ID;
import static com.cogent.cogentappointment.client.query.HospitalPatientInfoQuery.QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO;
import static com.cogent.cogentappointment.client.query.HospitalPatientInfoQuery.QUERY_TO_FETCH_HOSPITAL_PATIENT_INFO_COUNT;

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
            log.error(ex.getLocalizedMessage());
            return null;
        }
    }
}
