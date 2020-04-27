package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.constants.ErrorMessageConstants.AdminServiceMessages;
import com.cogent.cogentappointment.logging.dto.response.LoggedInAdminDTO;
import com.cogent.cogentappointment.logging.exception.NoContentFoundException;
import com.cogent.cogentappointment.logging.repository.custom.AdminRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.logging.constants.QueryConstants.EMAIl;
import static com.cogent.cogentappointment.logging.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.logging.utils.AdminQuery.QUERY_TO_GET_LOGGED_ADMIN_INFO;
import static com.cogent.cogentappointment.logging.utils.AdminQuery.QUERY_TO_VALIDATE_ADMIN_COUNT;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.transformQueryToSingleResult;

/**
 * @author smriti on 7/21/19
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object[] validateAdminCount(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_ADMIN_COUNT)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        return results.get(0);
    }

    @Override
    public LoggedInAdminDTO getLoggedInAdmin(String email) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LOGGED_ADMIN_INFO)
                .setParameter(EMAIl, email);
        try {
            return transformQueryToSingleResult(query, LoggedInAdminDTO.class);
        } catch (NoResultException e) {
            throw ADMIN_NOT_FOUND.apply(email);
        }
    }

    private Function<String, NoContentFoundException> ADMIN_NOT_FOUND = (email) -> {
//        log.error(ADMIN_NOT_FOUND_ERROR, email);
        throw new NoContentFoundException(String.format(AdminServiceMessages.ADMIN_NOT_FOUND, email),
                "email", email);
    };


}

