package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.ClientFeatureIntegrationRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.API_FEATURE_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.API_REQUEST_METHOD_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.query.IntegrationQuery.VALIDATE_HOSPITAL_REQUEST_METHOD_AND_FEATURE;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;

/**
 * @author rupak ON 2020/06/02-9:46 AM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class ClientFeatureIntegrationRepositoryCustomImpl implements
        ClientFeatureIntegrationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long findHospitalFeatureAndRequestMethod(Long hospitalId,
                                                    Long featureTypeId,
                                                    Long requestMethodId) {
        Query query = createQuery.apply(entityManager,
                VALIDATE_HOSPITAL_REQUEST_METHOD_AND_FEATURE)
                .setParameter(API_FEATURE_ID, featureTypeId)
                .setParameter(HOSPITAL_ID, hospitalId)
                .setParameter(API_REQUEST_METHOD_ID, requestMethodId);


        return (Long) query.getSingleResult();
    }


}
