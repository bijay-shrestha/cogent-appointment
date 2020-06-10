package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiQueryParametersDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.ApiRequestHeaderDetailResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiQueryParametersUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ApiRequestHeaderUpdateResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author rupak on 2020-05-20
 */
@Repository
@Qualifier("integrationRepositoryCustom")
public interface IntegrationRepositoryCustom {

    List<ApiRequestHeaderDetailResponse> findApiRequestHeaders(Long featureId);

    List<ApiQueryParametersDetailResponse> findApiQueryParameters(Long featureId);

    Map<String,String> findApiRequestHeadersResponse(Long featureId);

    Map<String,String> findApiQueryParametersResponse(Long featureId);

    Map<String, String> findAdminModeApiRequestHeaders(Long apiIntegrationFormatId);

    Map<String, String> findAdminModeApiQueryParameters(Long apiIntegrationFormatId);

    ClientApiIntegrationSearchDTO searchClientApiIntegration(ClientApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable);

    ClientApiIntegrationResponseDTO findClientApiIntegration(Long id);

    List<ApiRequestHeaderUpdateResponseDTO> findApiRequestHeadersForUpdate(Long featureId);

    List<ApiQueryParametersUpdateResponseDTO> findApiQueryParametersForUpdate(Long featureId);

    List<ClientFeatureIntegrationResponse> fetchClientIntegrationResponseDTO();

    List<ClientFeatureIntegrationResponse>
    fetchClientIntegrationResponseDTOforBackendIntegration(IntegrationBackendRequestDTO integrationBackendRequestDTO);

    List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long featureId);
}
