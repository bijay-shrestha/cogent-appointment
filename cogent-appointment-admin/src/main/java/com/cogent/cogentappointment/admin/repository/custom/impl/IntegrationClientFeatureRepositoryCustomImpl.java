package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.ClientFeatureIntegrationRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

//    @Override
//    public Long findHospitalFeatureAndRequestMethod(Long hospitalId,
//                                                    Long featureTypeId,
//                                                    Long requestMethodId) {
//        Query query = createNativeQuery.apply(entityManager,
//                VALIDATE_HOSPITAL_REQUEST_METHOD_AND_FEATURE)
//                .setParameter(API_FEATURE__ID, featureTypeId)
//                .setParameter(HOSPITAL_ID, hospitalId)
//                .setParameter(API_REQUEST_METHOD_ID, requestMethodId);
//
//
//        return (Long) query.getSingleResult();
//    }
}
