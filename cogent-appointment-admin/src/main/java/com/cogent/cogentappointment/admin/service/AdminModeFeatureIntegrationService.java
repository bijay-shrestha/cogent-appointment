package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeIntegrationSearchDTO;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

/**
 * @author rupak on 2020-05-21
 */
public interface AdminModeFeatureIntegrationService {

    void save(AdminModeApiIntegrationRequestDTO requestDTO);

    AdminModeIntegrationSearchDTO search(AdminModeApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable);

    void delete(DeleteRequestDTO deleteRequestDTO);

    AdminModeIntegrationDetailResponseDTO fetchClientApiIntegrationById(Long id);
}
