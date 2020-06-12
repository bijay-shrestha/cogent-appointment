package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.integration.ApiRequestHeaderDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeRequestHeaderRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.API_FEATURE_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.CLIENT_API_INTEGRATION_FORMAT_ID;
import static com.cogent.cogentappointment.admin.query.IntegrationAdminModeQuery.ADMIN_MODE_FEATURES_HEADERS_DETAILS_QUERY;
import static com.cogent.cogentappointment.admin.query.IntegrationAdminModeQuery.ADMIN_MODE_FEATURES_HEADERS_QUERY;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak ON 2020/06/04-12:23 PM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminModeRequestHeaderRepositoryCustomImpl implements AdminModeRequestHeaderRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ApiRequestHeaderDetailResponse> findAdminModeApiRequestHeaders(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_FEATURES_HEADERS_DETAILS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderDetailResponse> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderDetailResponse.class);

        if (requestHeaderResponseDTO.isEmpty())
            return null;

        else {
            return requestHeaderResponseDTO;
        }

    }

    @Override
    public List<ApiRequestHeaderUpdateResponseDTO> findAdminModeApiRequestHeaderForUpdate(Long featureId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_FEATURES_HEADERS_QUERY)
                .setParameter(API_FEATURE_ID, featureId);

        List<ApiRequestHeaderUpdateResponseDTO> apiRequestHeaderUpdateResponseDTOS =
                transformQueryToResultList(query, ApiRequestHeaderUpdateResponseDTO.class);

        if (apiRequestHeaderUpdateResponseDTOS.isEmpty())
            return null;

        else {
            return apiRequestHeaderUpdateResponseDTOS;
        }

    }
}
