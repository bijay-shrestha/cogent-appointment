package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.Country;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
public interface CountryService {
    List<DropDownResponseDTO> fetchActiveCountry();

    List<DropDownResponseDTO> fetchCountry();

    Country fetchCountryById(Long id);
}
