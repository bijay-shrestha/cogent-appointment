package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.clientIntegration.ApiQueryParametersResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.ApiRequestHeaderResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.query.IntegrationQuery;
import com.cogent.cogentappointment.client.repository.custom.IntegrationRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.client.constants.QueryConstants.CLIENT_API_INTEGRATION_FORMAT_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak on 2020-05-20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class IntegrationRepositoryCustomImpl implements IntegrationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FeatureIntegrationResponse> fetchClientIntegrationResponseDTO(Long hospitalId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_FEAUTRES_INTEGRATION_API_QUERY)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<FeatureIntegrationResponse> responseDTOList =
                transformQueryToResultList(query, FeatureIntegrationResponse.class);

//        if (appointmentDetails.isEmpty()) throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
//
//        return appointmentDetails.get(0);

        return responseDTOList;
    }

    @Override
    public Map<String, String> findApiRequestHeaders(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_API_FEAUTRES_HEADERS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        requestHeaderResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

//        if (appointmentDetails.isEmpty()) throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
//
//        return appointmentDetails.get(0);

        return map;
    }

    @Override
    public Map<String, String> findApiQueryParameters(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, IntegrationQuery.CLIENT_API_PARAMETERS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        parametersResponseDTO.forEach(response -> {
            map.put(response.getParam(), response.getValue());
        });

//        if (appointmentDetails.isEmpty()) throw APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId);
//
//        return appointmentDetails.get(0);

        return map;
    }
}
