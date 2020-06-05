package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.ApiQueryParametersResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeQueryParametersRepositoryCustom;
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
    public Map<String, String> findAdminModeApiQueryParameters(Long featureId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_QUERY_PARAMETERS_QUERY)
                .setParameter(API_FEATURE_ID, featureId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        parametersResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
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
