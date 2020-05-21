package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.adminModeIntegration.FeatureIntegrationResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author rupak on 2020-05-20
 */
@Repository
@Qualifier("integrationRepositoryCustom")
public interface IntegrationRepositoryCustom {

    Map<String,String> findApiRequestHeaders(Long apiIntegrationFormatId);

    Map<String,String> findApiQueryParameters(Long apiIntegrationFormatId);


    List<FeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO(Long hospitalId);
}
