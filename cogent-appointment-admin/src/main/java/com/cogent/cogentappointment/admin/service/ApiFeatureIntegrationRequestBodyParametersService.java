package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.IntegrationClient.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.ApiFeatureIntegrationRequestBodyRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;

import javax.validation.Valid;
import java.util.List;

public interface ApiFeatureIntegrationRequestBodyParametersService {

    void save(@Valid ApiFeatureIntegrationRequestBodyRequestDTO requestDTO);

    List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long id);
}
