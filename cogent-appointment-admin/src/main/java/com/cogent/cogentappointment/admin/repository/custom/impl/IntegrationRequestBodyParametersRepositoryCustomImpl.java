package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.RequestBodyParametersQuery;
import com.cogent.cogentappointment.admin.repository.custom.IntegrationRequestBodyParametersRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.API_REQUEST_BODY_PARAMETERS;
import static com.cogent.cogentappointment.admin.query.RequestBodyParametersQuery.FETCH_REQUEST_BODY_ATTRIBUTE_BY_ID;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak ON 2020/05/29-10:23 AM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class IntegrationRequestBodyParametersRepositoryCustomImpl implements
        IntegrationRequestBodyParametersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveRequestBodyParameters() {
        Query query = createQuery.apply(entityManager, RequestBodyParametersQuery.QUERY_TO_FETCH_MIN_REQUEST_BODY_PARAMTERS);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw REQUEST_BODY_PARAMETERS.get();
        } else return results;
    }

    @Override
    public List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long featureId) {
        Query query = createQuery.apply(entityManager,
                RequestBodyParametersQuery.FETCH_REQUEST_BODY_ATTRIBUTE_BY_FEATURE_ID)
                .setParameter(QueryConstants.API_FEATURE__ID,featureId);

        int totalItems = query.getResultList().size();

        List<IntegrationRequestBodyAttributeResponse> bodyAttributeResponseList =
                transformQueryToResultList(query, IntegrationRequestBodyAttributeResponse.class);
        if (bodyAttributeResponseList.isEmpty()) {
            error();
            throw REQUEST_BODY_PARAMETERS.get();

        }else {
            return bodyAttributeResponseList;
        }
    }

    @Override
    public List<ApiIntegrationRequestBodyParameters> findActiveRequestBodyParameterByIds(String ids) {
        Query query = createQuery.apply(entityManager,
                FETCH_REQUEST_BODY_ATTRIBUTE_BY_ID(ids));

        List<ApiIntegrationRequestBodyParameters> bodyParametersList=query.getResultList();

        if (bodyParametersList.isEmpty()) {
            error();
            throw REQUEST_BODY_PARAMETERS.get();

        }else {
            return bodyParametersList;
        }
    }


    private Supplier<NoContentFoundException> REQUEST_BODY_PARAMETERS = () ->
            new NoContentFoundException(ApiIntegrationRequestBodyParameters.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, API_REQUEST_BODY_PARAMETERS);
    }
}
