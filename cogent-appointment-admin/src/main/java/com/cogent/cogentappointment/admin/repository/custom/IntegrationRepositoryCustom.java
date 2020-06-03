package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.FeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationSearchDTO;
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

    Map<String,String> findApiRequestHeaders(Long featureId);

    Map<String,String> findApiQueryParameters(Long featureId);

    List<FeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO(Long hospitalId);

    Map<String, String> findAdminModeApiRequestHeaders(Long apiIntegrationFormatId);

    Map<String, String> findAdminModeApiQueryParameters(Long apiIntegrationFormatId);

    ClientApiIntegrationSearchDTO searchClientApiIntegration(ClientApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable);

    ClientApiIntegrationResponseDTO findClientApiIntegration(Long id);

    List<ApiRequestHeaderUpdateResponseDTO> findApiRequestHeadersForUpdate(Long featureId);

    List<ApiQueryParametersUpdateResponseDTO> findApiQueryParametersForUpdate(Long featureId);
}
