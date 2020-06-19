package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.ApiQueryParametersResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.ApiRequestHeaderResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.IntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.IntegrationLog.CLIENT_FEATURE_INTEGRATION;
import static com.cogent.cogentappointment.client.query.IntegrationAdminModeQuery.*;
import static com.cogent.cogentappointment.client.query.IntegrationQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

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
        Query query = createQuery.apply(entityManager, CLIENT_FEATURES_INTEGRATION_API_QUERY)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<FeatureIntegrationResponse> responseDTOList =
                transformQueryToResultList(query, FeatureIntegrationResponse.class);

        return responseDTOList;
    }

    @Override
    public Map<String, String> findApiRequestHeaders(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_FEATURES_HEADERS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderResponseDTO.class);

        Map<String, String> map = new HashMap<>();

        requestHeaderResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public Map<String, String> findApiQueryParameters(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_PARAMETERS_QUERY)
                .setParameter(CLIENT_API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();

        parametersResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public Map<String, String> findAdminModeApiRequestHeaders(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_API_FEAUTRES_HEADERS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        requestHeaderResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public Map<String, String> findAdminModeApiQueryParameters(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, ADMIN_MODE_API_PARAMETERS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        parametersResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    @Override
    public FeatureIntegrationResponse fetchClientIntegrationResponseDTOforBackendIntegration(
            IntegrationBackendRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, CLIENT_FEATURES_INTEGRATION_BACKEND_API_QUERY)
                .setParameter(HOSPITAL_ID, getLoggedInHospitalId())
                .setParameter(INTEGRATION_CHANNEL_CODE, requestDTO.getIntegrationChannelCode())
                .setParameter(FEATURE_CODE, requestDTO.getFeatureCode());

        try {
            FeatureIntegrationResponse featureIntegrationResponse =
                    transformQueryToSingleResult(query, FeatureIntegrationResponse.class);
            return featureIntegrationResponse;
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AdminFeatureIntegrationResponse fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(IntegrationRefundRequestDTO refundRequestDTO,
                                                                                                           Long appointmentModeId) {
        Query query = createQuery.apply(entityManager, APPOINTMENT_MODE_FEATURES_INTEGRATION_BACKEND_API_QUERY)
                .setParameter(APPOINTMENT_MODE_ID, appointmentModeId)
                .setParameter(INTEGRATION_CHANNEL_CODE, refundRequestDTO.getIntegrationChannelCode())
                .setParameter(FEATURE_CODE, refundRequestDTO.getFeatureCode());

        AdminFeatureIntegrationResponse responseDTOList =
                transformQueryToSingleResult(query, AdminFeatureIntegrationResponse.class);

//        if (responseDTOList.isEmpty()) throw CLIENT_API_INTEGRATION_NOT_FOUND.get();

        return responseDTOList;
    }

    private Supplier<NoContentFoundException> CLIENT_API_INTEGRATION_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, CLIENT_FEATURE_INTEGRATION);
        throw new NoContentFoundException(ClientFeatureIntegration.class);
    };

}
