package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.integration.IntegrationBodyAttributeResponse;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("integrationRequestBodyParametersRepositoryCustom")
public interface IntegrationRequestBodyParametersRepositoryCustom {

    List<ApiIntegrationRequestBodyParameters> findActiveRequestBodyParameterByIds(String ids);


    List<IntegrationBodyAttributeResponse> fetchRequestBodyAttributeByFeatureId(Long featureId);

    List<IntegrationBodyAttributeResponse> fetchRequestBodyAttributes();
}
