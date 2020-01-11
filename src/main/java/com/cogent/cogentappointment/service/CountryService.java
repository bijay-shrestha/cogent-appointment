package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.model.Country;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
public interface CountryService {
    List<DropDownResponseDTO> fetchActiveCountry();

    Country fetchCountryById(Long id);
}
