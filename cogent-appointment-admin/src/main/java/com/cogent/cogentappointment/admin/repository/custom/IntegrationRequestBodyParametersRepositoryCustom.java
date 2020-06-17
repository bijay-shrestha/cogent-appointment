package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiIntegrationRequestBodySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute.ApiRequestBodySearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationRequestBodyAttribute.IntegrationRequestBodyDetailResponseDTO;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("integrationRequestBodyParametersRepositoryCustom")
public interface IntegrationRequestBodyParametersRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveRequestBodyParameters();

    List<IntegrationRequestBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long id);

    List<ApiIntegrationRequestBodyParameters> findActiveRequestBodyParameterByIds( String ids);

    List<IntegrationBodyAttributeResponse> fetchRequestBodyAttributes();


    ApiRequestBodySearchDTO searchApiRequestBodyAtrributes(ApiIntegrationRequestBodySearchRequestDTO searchRequestDTO, Pageable pageable);

    IntegrationRequestBodyDetailResponseDTO fetchRequestBodyAttributeDetails(Long featureId);
}
