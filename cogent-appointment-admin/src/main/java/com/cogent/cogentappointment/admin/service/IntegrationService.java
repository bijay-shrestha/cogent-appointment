package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationUpdateRequestDTO;

import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
public interface IntegrationService {

    void save(ClientApiIntegrationRequestDTO requestDTO);

    void update(ClientApiIntegrationUpdateRequestDTO requestDTO);

//    ClientApiIntegrationRequestDTO fetchClientIntegrationResponseDTO(ClientApiIntegrationRequestDTO );
}
