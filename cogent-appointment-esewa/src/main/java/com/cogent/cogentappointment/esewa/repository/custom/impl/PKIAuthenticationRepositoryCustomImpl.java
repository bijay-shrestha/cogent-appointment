package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.PKIAuthenticationInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.CLIENT_ID;
import static com.cogent.cogentappointment.esewa.query.PKIAuthenticationInfoQuery.QUERY_TO_FETCH_PKI_CLIENT_PUBLIC_KEY;
import static com.cogent.cogentappointment.esewa.query.PKIAuthenticationInfoQuery.QUERY_TO_FETCH_PKI_SERVER_PRIVATE_KEY;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToSingleResult;

/**
 * @author smriti on 06/07/20
 */
@Repository
//@Transactional
//        (readOnly = true)
@Slf4j
public class PKIAuthenticationRepositoryCustomImpl implements PKIAuthenticationInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String findServerPrivateKeyByClientId(String clientId) {

        Query query = entityManager.createQuery(QUERY_TO_FETCH_PKI_SERVER_PRIVATE_KEY)
                .setParameter(CLIENT_ID, clientId);
        try {

            return (String) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            throw new NoContentFoundException("Client key not found");
        }
    }

    @Override
    public String findClientPublicKeyByClientId(String clientId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PKI_CLIENT_PUBLIC_KEY)
                .setParameter(CLIENT_ID, clientId);

        try {
            return (String) query.getSingleResult();
        } catch (NoResultException ex) {
            ex.printStackTrace();
            throw new NoContentFoundException("Access key not found");
        }
    }
}
