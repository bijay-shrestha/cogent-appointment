package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.model.Country;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
public interface CountryService {
    List<DropDownResponseDTO> fetchActiveCountry();

    Country fetchCountryById(Long id);
}
