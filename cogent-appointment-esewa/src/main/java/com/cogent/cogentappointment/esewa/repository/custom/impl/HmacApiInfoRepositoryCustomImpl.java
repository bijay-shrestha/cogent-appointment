package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HmacApiInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.INVALID_COMPANY_CODE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.API_KEY;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.COMPANY_CODE;
import static com.cogent.cogentappointment.esewa.query.HmacApiInfoQuery.QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToSingleResult;

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
    public ThirdPartyDetail getDetailForAuthentication(String companyCode, String apiKey) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_THIRD_PARTY_INFO_FOR_AUTHENTICATION)
                .setParameter(API_KEY, apiKey)
                .setParameter(COMPANY_CODE, companyCode);

        try {
            return transformQueryToSingleResult(query, ThirdPartyDetail.class);
        } catch (NoResultException e) {
            log.error(INVALID_COMPANY_CODE);
            throw new NoContentFoundException(INVALID_COMPANY_CODE);
        }
    }

}
