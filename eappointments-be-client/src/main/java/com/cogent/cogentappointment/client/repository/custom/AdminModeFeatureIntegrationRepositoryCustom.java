package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeApiIntegrationResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rupak ON 2020/06/03-1:58 PM
 */
@Repository
@Qualifier("adminModeFeatureIntegrationRepositoryCustom")
public interface AdminModeFeatureIntegrationRepositoryCustom {


    List<AdminFeatureIntegrationResponse> fetchAdminModeIntegrationResponseDTO(Long hospitalId);

    AdminModeApiIntegrationResponseDTO findAdminModeFeatureIntegration(Long id);
}
