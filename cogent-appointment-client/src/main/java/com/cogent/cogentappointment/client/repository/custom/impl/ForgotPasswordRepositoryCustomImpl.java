package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.query.ForgotPasswordVerificationQuery;
import com.cogent.cogentappointment.client.repository.custom.ForgotPasswordRepositoryCustom;
import com.cogent.cogentappointment.client.utils.commons.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.EmailConstants.ForgotPassword.RESET_CODE;

/**
 * @author smriti on 2019-09-20
 */
@Repository
@Transactional(readOnly = true)
public class ForgotPasswordRepositoryCustomImpl implements ForgotPasswordRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object findByResetCode(String resetCode) {

        Query query = QueryUtils.createQuery.apply(entityManager, ForgotPasswordVerificationQuery.QUERY_TO_FETCH_EXPIRATION_TIME)
                .setParameter(RESET_CODE, resetCode);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw RESET_CODE_NOT_FOUND.apply(resetCode);
        }
    }

    private Function<String, NoContentFoundException> RESET_CODE_NOT_FOUND = (resetCode) -> {
        throw new NoContentFoundException(ErrorMessageConstants.ForgotPasswordMessages.INVALID_RESET_CODE, "resetCode", resetCode);
    };

}
