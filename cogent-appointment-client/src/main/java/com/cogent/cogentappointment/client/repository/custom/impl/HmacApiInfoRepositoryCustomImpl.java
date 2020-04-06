package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.HmacApiInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_USERNAME_OR_HOSPITAL_CODE;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.HmacApiInfoQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToSingleResult;

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
    public ThirdPartyDetail getDetailForAuthentication(String hospitalCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION)
                .setParameter(API_KEY, apiKey)
                .setParameter(HOSPITAL_CODE, hospitalCode);
        try {
            return transformQueryToSingleResult(query, ThirdPartyDetail.class);
        } catch (NoResultException e) {
            log.error(INVALID_USERNAME_OR_HOSPITAL_CODE);
            throw new NoContentFoundException(INVALID_USERNAME_OR_HOSPITAL_CODE);
        }
    }

    @Override
    public ThirdPartyDetail getDetailsByHospitalCode(String hospitalCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_HMAC_GENERATION)
                .setParameter(HOSPITAL_CODE, hospitalCode);
        try {
            return transformQueryToSingleResult(query, ThirdPartyDetail.class);
        } catch (NoResultException e) {
            log.error(INVALID_USERNAME_OR_HOSPITAL_CODE);
            throw new NoContentFoundException(INVALID_USERNAME_OR_HOSPITAL_CODE);
        }
    }

    @Override
    public AdminMinDetails verifyLoggedInAdmin(String username, String hospitalCode) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VERIFY_LOGGED_IN_ADMIN)
                .setParameter(USERNAME, username)
                .setParameter(HOSPITAL_CODE, hospitalCode);
        try {
            return transformQueryToSingleResult(query, AdminMinDetails.class);
        } catch (NoResultException e) {
            log.error(INVALID_USERNAME_OR_HOSPITAL_CODE);
            throw new NoContentFoundException(INVALID_USERNAME_OR_HOSPITAL_CODE);
        }
    }

    @Override
    public AdminMinDetails getAdminDetailForAuthentication(String username, String hospitalCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FECTH_ADMIN_FOR_AUTHENTICATION)
                .setParameter(USERNAME, username)
                .setParameter(HOSPITAL_CODE, hospitalCode)
                .setParameter(API_KEY, apiKey);
        try {
            return transformQueryToSingleResult(query, AdminMinDetails.class);
        } catch (NoResultException e) {
            log.error(INVALID_USERNAME_OR_HOSPITAL_CODE);
            throw new NoContentFoundException(INVALID_USERNAME_OR_HOSPITAL_CODE);
        }
    }
}
