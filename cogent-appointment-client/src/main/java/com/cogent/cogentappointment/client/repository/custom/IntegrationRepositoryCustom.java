package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rupak on 2020-05-20
 */
@Repository
@Qualifier("integrationRepositoryCustom")
public interface IntegrationRepositoryCustom {

    List<FeatureIntegrationResponse> fetchClientIntegrationResponseDTO(Long hospitalId);

    List<ApiRequestHeaderResponseDTO> findApiRequestHeaders(Long apiIntegrationFormatId);

    List<ApiQueryParametersResponseDTO> findApiQueryParameters(Long apiIntegrationFormatId);
}
