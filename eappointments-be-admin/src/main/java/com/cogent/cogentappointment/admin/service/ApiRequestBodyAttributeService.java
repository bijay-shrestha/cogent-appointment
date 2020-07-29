package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiFeatureIntegrationRequestBodyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.integrationRequestBodyAttributeUpdate.ApiFeatureIntegrationRequestBodyUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiIntegrationRequestBodySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute.ApiRequestBodySearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute.IntegrationRequestBodyDetailResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApiRequestBodyAttributeService {

    void save(ApiFeatureIntegrationRequestBodyRequestDTO requestDTO);

    List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long id);

    ApiRequestBodySearchDTO search(ApiIntegrationRequestBodySearchRequestDTO searchRequestDTO,
                                   Pageable pageable);

    void delete(DeleteRequestDTO deleteRequestDTO);

    void update(ApiFeatureIntegrationRequestBodyUpdateRequestDTO requestDTO);

    IntegrationRequestBodyDetailResponseDTO fetchRequestBodyAttributeDetails(Long featureId);
}
