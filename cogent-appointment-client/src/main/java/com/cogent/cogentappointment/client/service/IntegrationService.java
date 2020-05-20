package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.ClientIntegrationResponseDTO;

/**
 * @author rupak on 2020-05-19
 */
public interface IntegrationService {

    ClientIntegrationResponseDTO fetchClientIntegrationResponseDTO(ApiIntegrationRequestDTO apiIntegrationRequestDTO);
}
