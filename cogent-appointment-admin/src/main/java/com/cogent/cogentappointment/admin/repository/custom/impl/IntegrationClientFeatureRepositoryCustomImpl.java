package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.query.IntegrationQuery;
import com.cogent.cogentappointment.admin.repository.custom.ClientFeatureIntegrationRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createNativeQuery;

/**
 * @author rupak ON 2020/06/02-9:46 AM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class IntegrationClientFeatureRepositoryCustomImpl implements
        ClientFeatureIntegrationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long findByHospitalWiseFeatureIdAndRequestMethod(Long hospitalId,
                                                            Long featureTypeId,
                                                            Long requestMethodId) {
        Query query = createNativeQuery.apply(entityManager,
                IntegrationQuery.VALIDATE_HOSPITAL_REQUEST_METHOD_AND_FEATURE)
                .setParameter(API_FEATURE__ID, featureTypeId)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(API_REQUEST_METHOD_ID, requestMethodId);


        return (Long) query.getSingleResult();
    }
}
