package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.BreakType;

import java.util.List;

/**
 * @author smriti on 06/05/20
 */
public interface BreakTypeService {

    List<DropDownResponseDTO> fetchBreakTypeByHospitalId(Long hospitalId);

    BreakType fetchActiveBreakType(Long id);
}
