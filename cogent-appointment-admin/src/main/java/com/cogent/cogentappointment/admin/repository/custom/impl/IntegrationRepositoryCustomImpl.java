package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiQueryParametersDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiRequestHeaderDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.ApiQueryParametersResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.ApiRequestHeaderResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationSearchResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.IntegrationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ApiQueryParameters;
import com.cogent.cogentappointment.persistence.model.ApiRequestHeader;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NO_RECORD_FOUND;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.CLIENT;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.admin.log.constants.IntegrationLog.*;
import static com.cogent.cogentappointment.admin.query.IntegrationAdminModeQuery.*;
import static com.cogent.cogentappointment.admin.query.IntegrationQuery.*;
import static com.cogent.cogentappointment.admin.query.RequestBodyAttributesQuery.FETCH_REQUEST_BODY_ATTRIBUTE_BY_FEATURE_ID;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

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
    public ClientApiIntegrationSearchDTO searchClientApiIntegration(ClientApiIntegrationSearchRequestDTO searchRequestDTO,
                                                                    Pageable pageable) {

        Query query = createQuery.apply(entityManager, CLIENT_API_INTEGRATION_SEARCH_QUERY.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<ClientApiIntegrationSearchResponseDTO> apiIntegrationSearchResponseDTOList =
                transformQueryToResultList(query, ClientApiIntegrationSearchResponseDTO.class);

        ClientApiIntegrationSearchDTO integrationSearchDTO = new ClientApiIntegrationSearchDTO();
        if (apiIntegrationSearchResponseDTOList.isEmpty())
            throw CLIENT_API_INTEGRATION_NOT_FOUND.get();

        else {
            integrationSearchDTO.setSearchResponseDTOS(apiIntegrationSearchResponseDTOList);
            integrationSearchDTO.setTotalItems(totalItems);
            return integrationSearchDTO;
        }

    }

    @Override
    public ClientApiIntegrationResponseDTO findClientApiIntegration(Long id) {
        Query query = createQuery.apply(entityManager, CLIENT_FEATURES_INTEGRATION_DETAILS_API_QUERY)
                .setParameter(CLIENT_FEATURE_INTEGRATION_ID, id);

        try {
            return transformQueryToSingleResult(query, ClientApiIntegrationResponseDTO.class);
        } catch (NoResultException e) {
            throw CLIENT_API_FEATURE_INTEGRATION.apply(id);
        }

    }

    @Override
    public List<ApiRequestHeaderUpdateResponseDTO> findApiRequestHeadersForUpdate(Long apiIntegrationFormatId) {

        Query query = createQuery.apply(entityManager, CLIENT_API_FEATURES_HEADERS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderUpdateResponseDTO> apiRequestHeaderUpdateResponseDTOS =
                transformQueryToResultList(query, ApiRequestHeaderUpdateResponseDTO.class);

        if (apiRequestHeaderUpdateResponseDTOS.isEmpty())
            return null;

        else {
            return apiRequestHeaderUpdateResponseDTOS;
        }


    }

    @Override
    public List<ApiQueryParametersUpdateResponseDTO> findApiQueryParametersForUpdate(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_PARAMETERS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersUpdateResponseDTO> apiQueryParametersUpdateResponseDTOS =
                transformQueryToResultList(query, ApiQueryParametersUpdateResponseDTO.class);

        if (apiQueryParametersUpdateResponseDTOS.isEmpty())
            return null;

        else {
            return apiQueryParametersUpdateResponseDTOS;
        }

    }

    @Override
    public List<ClientFeatureIntegrationResponse> fetchClientIntegrationResponseDTO() {
        Query query = createQuery.apply(entityManager, CLIENT_FEAUTRES_INTEGRATION_API_QUERY);

        List<ClientFeatureIntegrationResponse> responseDTOList =
                transformQueryToResultList(query, ClientFeatureIntegrationResponse.class);

        return responseDTOList;
    }

    @Override
    public ClientFeatureIntegrationResponse fetchClientIntegrationResponseDTOForBackendIntegration(
            IntegrationBackendRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, CLIENT_FEATURES_INTEGRATION_BACKEND_API_QUERY)
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId())
                .setParameter(INTEGRATION_CHANNEL_CODE, requestDTO.getIntegrationChannelCode())
                .setParameter(FEATURE_CODE, requestDTO.getFeatureCode());

        try {
            return transformQueryToSingleResult(query, ClientFeatureIntegrationResponse.class);
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AdminFeatureIntegrationResponse fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(
            IntegrationBackendRequestDTO requestDTO,
            Long appointmentModeId) {

        Query query = createQuery.apply(entityManager, APPOINTMENT_MODE_FEATURES_INTEGRATION_BACKEND_API_QUERY)
                .setParameter(APPOINTMENT_MODE_ID, appointmentModeId)
                .setParameter(INTEGRATION_CHANNEL_CODE, requestDTO.getIntegrationChannelCode())
                .setParameter(FEATURE_CODE, requestDTO.getFeatureCode());

        AdminFeatureIntegrationResponse responseDTOList =
                transformQueryToSingleResult(query, AdminFeatureIntegrationResponse.class);

//        if (responseDTOList.isEmpty()) throw CLIENT_API_INTEGRATION_NOT_FOUND.get();

        return responseDTOList;
    }

    @Override
    public List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long featureId) {
        Query query = createQuery.apply(entityManager,
                FETCH_REQUEST_BODY_ATTRIBUTE_BY_FEATURE_ID)
                .setParameter(API_FEATURE_ID, featureId);

        List<IntegrationRequestBodyAttributeResponse> bodyAttributeResponseList =
                transformQueryToResultList(query, IntegrationRequestBodyAttributeResponse.class);
        if (bodyAttributeResponseList.isEmpty()) {
//            error();
            return null;

        } else {
            return bodyAttributeResponseList;
        }
    }

    @Override
    public List<ApiRequestHeaderDetailResponse> findApiRequestHeaders(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_FEATURES_HEADERS_DETAILS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiRequestHeaderDetailResponse> requestHeaderResponseDTO =
                transformQueryToResultList(query, ApiRequestHeaderDetailResponse.class);

        return requestHeaderResponseDTO;
    }

    @Override
    public List<ApiQueryParametersDetailResponse> findApiQueryParameters(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_PARAMETERS_DETAILS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersDetailResponse> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersDetailResponse.class);

        return parametersResponseDTO;
    }

    @Override
    public Map<String, String> findApiRequestHeadersResponse(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_FEATURES_HEADERS_QUERY)
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
    public Map<String, String> findApiQueryParametersResponse(Long apiIntegrationFormatId) {
        Query query = createQuery.apply(entityManager, CLIENT_API_PARAMETERS_QUERY)
                .setParameter(API_INTEGRATION_FORMAT_ID, apiIntegrationFormatId);

        List<ApiQueryParametersResponseDTO> parametersResponseDTO =
                transformQueryToResultList(query, ApiQueryParametersResponseDTO.class);

        Map<String, String> map = new HashMap<>();
        parametersResponseDTO.forEach(response -> {
            map.put(response.getKeyParam(), response.getValueParam());
        });

        return map;
    }

    private Function<Long, NoContentFoundException> CLIENT_API_FEATURE_INTEGRATION = (id) -> {
        log.error(CONTENT_NOT_FOUND, CLIENT_FEATURE_INTEGRATION);
        throw new NoContentFoundException
                (ClientFeatureIntegration.class, "id", id.toString());
    };

    private Supplier<NoContentFoundException> CLIENT_API_INTEGRATION_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, CLIENT_FEATURE_INTEGRATION);
        throw new NoContentFoundException(ClientFeatureIntegration.class);
    };

    private Function<Long, NoContentFoundException> CLIENT_API_REQUEST_HEADER_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND, API_REQUEST_HEADER);
        throw new NoContentFoundException(ApiRequestHeader.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> CLIENT_API_QUERY_PARAMETERS_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND, API_QUERY_PARAMETER);
        throw new NoContentFoundException(ApiQueryParameters.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND, CLIENT), "id", id.toString());
    };


}
