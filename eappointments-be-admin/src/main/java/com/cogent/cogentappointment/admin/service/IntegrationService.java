package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientApiIntegrationSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.clientIntegrationUpdate.ClientApiIntegrationUpdateResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author rupak on 2020-05-19
 */
public interface IntegrationService {

    void save(ClientApiIntegrationRequestDTO requestDTO);

    void update(ClientApiIntegrationUpdateRequestDTO requestDTO);

    ClientApiIntegrationSearchDTO search(ClientApiIntegrationSearchRequestDTO searchRequestDTO, Pageable pageable);

    ClientApiIntegrationDetailResponseDTO fetchClientApiIntegrationById(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    ClientApiIntegrationUpdateResponseDTO fetchDetailsForUpdate(Long id);
}
