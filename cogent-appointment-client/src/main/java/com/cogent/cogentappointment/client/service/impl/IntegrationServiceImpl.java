package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.*;
import com.cogent.cogentappointment.client.repository.IntegrationRepository;
import com.cogent.cogentappointment.client.service.IntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationRepository integrationRepository;

    public IntegrationServiceImpl(IntegrationRepository integrationRepository) {
        this.integrationRepository = integrationRepository;
    }

    @Override
    public ClientIntegrationResponseDTO fetchClientIntegrationResponseDTO(ApiIntegrationRequestDTO apiIntegrationRequestDTO) {

        List<FeatureIntegrationResponse> integrationResponseDTOList=integrationRepository.
                fetchClientIntegrationResponseDTO(apiIntegrationRequestDTO.getHospitalId());


        List<FeatureIntegrationResponseDTO> features=new ArrayList<>();
        integrationResponseDTOList.forEach(responseDTO->{

            List<ApiRequestHeaderResponseDTO> requestHeaderResponseDTO=integrationRepository.findApiRequestHeaders(responseDTO.getApiIntegrationFormatId());

            List<ApiQueryParametersResponseDTO> queryParametersResponseDTO=integrationRepository.findApiQueryParameters(responseDTO.getApiIntegrationFormatId());


            ClientIntegrationResponseDTO integrationResponseDTO=new ClientIntegrationResponseDTO();

            FeatureIntegrationResponseDTO featureIntegrationResponseDTO=new FeatureIntegrationResponseDTO();
            featureIntegrationResponseDTO.setFeatureCode(responseDTO.getFeatureCode());
            featureIntegrationResponseDTO.setRequestBody(responseDTO.getRequestBody());
            featureIntegrationResponseDTO.setRequestMethod(responseDTO.getRequestMethod());
            featureIntegrationResponseDTO.setUrl(responseDTO.getUrl());

            featureIntegrationResponseDTO.setHeaders(requestHeaderResponseDTO);
            featureIntegrationResponseDTO.setQueryParameters(queryParametersResponseDTO);

            features.add(featureIntegrationResponseDTO);

        });


        ClientIntegrationResponseDTO clientIntegrationResponseDTO=new ClientIntegrationResponseDTO();
        clientIntegrationResponseDTO.setFeatures(features);

        return clientIntegrationResponseDTO;

    }
}
