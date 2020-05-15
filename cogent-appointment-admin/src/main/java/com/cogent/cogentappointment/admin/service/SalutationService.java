package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.Salutation;

import java.util.List;

public interface SalutationService {

    List<DropDownResponseDTO> fetchActiveMinSalutation();

    Salutation fetchSalutationById(Long salutationId);
}
