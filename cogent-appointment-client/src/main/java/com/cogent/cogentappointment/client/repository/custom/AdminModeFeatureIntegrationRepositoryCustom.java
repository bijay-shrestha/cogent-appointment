package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeApiIntegrationResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author rupak ON 2020/06/03-1:58 PM
 */
@Repository
@Qualifier("adminModeFeatureIntegrationRepositoryCustom")
public interface AdminModeFeatureIntegrationRepositoryCustom {


    AdminFeatureIntegrationResponse fetchAdminModeIntegrationResponseDTO();

    AdminModeApiIntegrationResponseDTO findAdminModeFeatureIntegration(Long id);
}
