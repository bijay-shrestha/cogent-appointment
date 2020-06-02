package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.IntegrationQuery;
import com.cogent.cogentappointment.admin.repository.custom.HttpRequestMethodRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HttpRequestMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.HTTP_REQUEST_METHODS;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak on 2020-05-19
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HttpRequestMethodRepositoryCustomImpl implements HttpRequestMethodRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveRequestMethod() {
        Query query = createQuery.apply(entityManager, IntegrationQuery.QUERY_TO_FETCH_MIN_REQUEST_METHODS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            httpRequestMethodError();
            throw REQUEST_METHODS_NOT_FOUND.get();
        } else return results;
    }

    private Supplier<NoContentFoundException> REQUEST_METHODS_NOT_FOUND = () ->
            new NoContentFoundException(HttpRequestMethod.class);

    private void httpRequestMethodError() {
        log.error(CONTENT_NOT_FOUND, HTTP_REQUEST_METHODS);
    }
}
