package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.ApiRequestHeaderResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeRequestHeaderRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.API_FEATURE_ID;
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
    public List<ApiRequestHeaderResponseDTO> findAdminModeApiRequestHeaders(Long featureId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_FEATURES_HEADERS_QUERY)
                .setParameter(API_FEATURE_ID, featureId);

        List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderResponseDTO.class);

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
