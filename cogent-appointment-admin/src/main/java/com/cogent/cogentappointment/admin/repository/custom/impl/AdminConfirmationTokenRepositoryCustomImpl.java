package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.query.AdminConfirmationTokenQuery;
import com.cogent.cogentappointment.admin.repository.custom.AdminConfirmationTokenRepositoryCustom;
import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.function.Function;

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
        Query query = QueryUtils.createQuery.apply(entityManager, AdminConfirmationTokenQuery.QUERY_TO_FETCH_CONFIRMATION_TOKEN_STATUS)
                .setParameter(QueryConstants.CONFIRMATION_TOKEN, confirmationToken);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw CONFIRMATION_TOKEN_NOT_FOUND.apply(confirmationToken);
        }
    }

    private Function<String, NoContentFoundException> CONFIRMATION_TOKEN_NOT_FOUND = (confirmationToken) -> {
        throw new NoContentFoundException(ErrorMessageConstants.AdminServiceMessages.INVALID_CONFIRMATION_TOKEN, "confirmationToken", confirmationToken);
    };
}
