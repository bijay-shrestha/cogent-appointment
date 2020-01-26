package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.repository.custom.AdminConfirmationTokenRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.function.Function;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.AdminServiceMessages.INVALID_CONFIRMATION_TOKEN;
import static com.cogent.cogentappointment.constants.QueryConstants.CONFIRMATION_TOKEN;
import static com.cogent.cogentappointment.query.AdminConfirmationTokenQuery.QUERY_TO_FETCH_CONFIRMATION_TOKEN_STATUS;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 2019-09-23
 */
@Repository
@Transactional(readOnly = true)
public class AdminConfirmationTokenRepositoryCustomImpl implements AdminConfirmationTokenRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object findByConfirmationToken(String confirmationToken) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_CONFIRMATION_TOKEN_STATUS)
                .setParameter(CONFIRMATION_TOKEN, confirmationToken);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw CONFIRMATION_TOKEN_NOT_FOUND.apply(confirmationToken);
        }
    }

    private Function<String, NoContentFoundException> CONFIRMATION_TOKEN_NOT_FOUND = (confirmationToken) -> {
        throw new NoContentFoundException(INVALID_CONFIRMATION_TOKEN, "confirmationToken", confirmationToken);
    };
}
