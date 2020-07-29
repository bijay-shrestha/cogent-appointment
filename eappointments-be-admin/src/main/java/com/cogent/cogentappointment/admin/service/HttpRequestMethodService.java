package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
public interface HttpRequestMethodService {

    List<DropDownResponseDTO> fetchActiveRequestMethod();
}
