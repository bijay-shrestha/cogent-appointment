package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.IntegrationQuery;
import com.cogent.cogentappointment.admin.repository.custom.ApiIntegrationTypeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.CLIENT_API_INTEGRATION_FEATURE_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.CLIENT_API_INTEGRATION_TYPE_ID;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak on 2020-05-26
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class ApiIntegrationTypeRepositoryCustomImpl implements ApiIntegrationTypeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveApiIntegrationType() {
        Query query = createQuery.apply(entityManager, IntegrationQuery.QUERY_TO_FETCH_MIN_API_INTEGRATION_TYPE);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            throw API_INTEGRATION_TYPE_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveFeatureTypeByIntegrationTypeId(Long id) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.QUERY_TO_FETCH_MIN_API_INTEGRATION_TYPE_BY_INTEGRATION_TYPE_ID)
                .setParameter(CLIENT_API_INTEGRATION_TYPE_ID, id);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            throw API_INTEGRATION_TYPE_NOT_FOUND.get();
        } else return results;
    }

    private Supplier<NoContentFoundException> API_INTEGRATION_TYPE_NOT_FOUND = () ->
            new NoContentFoundException(ApiIntegrationType.class);
}
