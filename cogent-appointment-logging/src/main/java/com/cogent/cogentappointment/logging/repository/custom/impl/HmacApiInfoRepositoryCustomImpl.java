package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.logging.exception.NoContentFoundException;
import com.cogent.cogentappointment.logging.repository.custom.HmacApiInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.logging.constants.ErrorMessageConstants.INVALID_USERNAME_OR_HOSPITAL_CODE;
import static com.cogent.cogentappointment.logging.constants.QueryConstants.*;
import static com.cogent.cogentappointment.logging.query.HmacApiInfoQuery.QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION_FOR_ADMIN;
import static com.cogent.cogentappointment.logging.query.HmacApiInfoQuery.QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION_FOR_CLIENT;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.transformQueryToSingleResult;

/**
 * @author Sauravi Thapa २०/२/२
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HmacApiInfoRepositoryCustomImpl implements HmacApiInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AdminMinDetails getAdminDetailForAuthentication(String email, String hospitalCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION_FOR_ADMIN)
                .setParameter(EMAIl, email)
                .setParameter(HOSPITAL_CODE, hospitalCode)
                .setParameter(API_KEY, apiKey);
        try {
            return transformQueryToSingleResult(query, AdminMinDetails.class);
        } catch (NoResultException e) {
           return null;
        }
    }

    @Override
    public AdminMinDetails getAdminDetailForAuthenticationForClient(String email, String hospitalCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION_FOR_CLIENT)
                .setParameter(EMAIl, email)
                .setParameter(HOSPITAL_CODE, hospitalCode)
                .setParameter(API_KEY, apiKey);
        try {
            return transformQueryToSingleResult(query, AdminMinDetails.class);
        } catch (NoResultException e) {
            return null;
        }
    }
}
