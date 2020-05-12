package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.Salutation;

import java.util.List;

public interface SalutationService {

    List<DropDownResponseDTO> fetchActiveMinSalutation();

    Salutation fetchSalutationById(Long salutationId);
}
