package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationRequestDTO;

import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
public interface IntegrationService {

    void save(ClientApiIntegrationRequestDTO requestDTO);
}
