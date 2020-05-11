package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;

import java.util.List;

public interface SalutationService {

    List<DropDownResponseDTO> fetchActiveMinSalutation();
}
