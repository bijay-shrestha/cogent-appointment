package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.repository.custom.PatientRelationInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.PatientQueryConstants.CHILD_PATIENT_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.PatientQueryConstants.PARENT_PATIENT_ID;
import static com.cogent.cogentappointment.esewa.query.PatientRelationInfoQuery.QUERY_TO_FETCH_PATIENT_RELATION_INFO;

/**
 * @author smriti on 28/02/20
 */
@Repository
@Transactional(readOnly = true)
public class PatientRelationInfoRepositoryCustomImpl implements PatientRelationInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PatientRelationInfo fetchPatientRelationInfo(Long parentPatientId, Long childPatientId) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_PATIENT_RELATION_INFO, PatientRelationInfo.class)
                    .setParameter(PARENT_PATIENT_ID, parentPatientId)
                    .setParameter(CHILD_PATIENT_ID, childPatientId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
