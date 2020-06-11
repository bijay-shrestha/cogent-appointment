package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.integration.ApiQueryParametersDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeQueryParametersRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.API_FEATURE_ID;
import static com.cogent.cogentappointment.admin.query.IntegrationAdminModeQuery.ADMIN_MODE_QUERY_PARAMETERS_DETAILS_QUERY;
import static com.cogent.cogentappointment.admin.query.IntegrationAdminModeQuery.ADMIN_MODE_QUERY_PARAMETERS_QUERY;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak ON 2020/06/04-12:14 PM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminModeQueryParametersRepositoryCustomImpl implements AdminModeQueryParametersRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ApiQueryParametersDetailResponse> findAdminModeApiQueryParameters(Long featureId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_QUERY_PARAMETERS_DETAILS_QUERY)
                .setParameter(API_FEATURE_ID, featureId);

        List<ApiQueryParametersDetailResponse> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersDetailResponse.class);


        return parametersResponseDTO;
    }

    @Override
    public List<ApiQueryParametersUpdateResponseDTO> findAdminModeApiQueryParameterForUpdate(Long featureId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_QUERY_PARAMETERS_QUERY)
                .setParameter(API_FEATURE_ID, featureId);

        List<ApiQueryParametersUpdateResponseDTO> apiQueryParametersUpdateResponseDTOS =
                transformQueryToResultList(query, ApiQueryParametersUpdateResponseDTO.class);

        if (apiQueryParametersUpdateResponseDTOS.isEmpty())
//            throw CLIENT_API_REQUEST_HEADER_NOT_FOUND.get();
            return null;

        else {
            return apiQueryParametersUpdateResponseDTOS;
        }
    }


}
