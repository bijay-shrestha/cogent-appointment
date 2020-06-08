package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.integrationAdminModeUpdate.AdminModeIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationUpdateResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author rupak on 2020-05-21
 */
public interface AdminModeFeatureIntegrationService {

    void save(AdminModeApiIntegrationRequestDTO requestDTO);

    AdminModeIntegrationSearchDTO search(AdminModeApiIntegrationSearchRequestDTO searchRequestDTO,
                                         Pageable pageable);

    void delete(DeleteRequestDTO deleteRequestDTO);

    AdminModeIntegrationDetailResponseDTO fetchAdminModeIntegrationById(Long id);

    void update(AdminModeIntegrationUpdateRequestDTO requestDTO);

    AdminModeIntegrationUpdateResponseDTO fetchDetailsForUpdate(Long id);
}
