package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author rupak on 2020-05-26
 */
public interface ApiIntegrationTypeService {

    List<DropDownResponseDTO> fetchActiveApiIntegrationType();

    List<DropDownResponseDTO> fetchActiveFeatureTypeByIntegrationTypeId(Long id);
}
