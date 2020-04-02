package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.ForgotPasswordRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.EmailConstants.ForgotPassword.RESET_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.ForgotPasswordMessages.INVALID_RESET_CODE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.INVALID_RESET_CODE_ERROR;
import static com.cogent.cogentappointment.admin.query.ForgotPasswordVerificationQuery.QUERY_TO_FETCH_EXPIRATION_TIME;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;

/**
 * @author smriti on 2019-09-20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class ForgotPasswordRepositoryCustomImpl implements ForgotPasswordRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object findByResetCode(String resetCode) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXPIRATION_TIME)
                .setParameter(RESET_CODE, resetCode);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw RESET_CODE_NOT_FOUND.apply(resetCode);
        }
    }

    private Function<String, NoContentFoundException> RESET_CODE_NOT_FOUND = (resetCode) -> {
        log.error(INVALID_RESET_CODE_ERROR, resetCode);
        throw new NoContentFoundException(INVALID_RESET_CODE, "resetCode", resetCode);
    };

}
