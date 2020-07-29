package com.cogent.cogentappointment.commons.service;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Sauravi Thapa ON 6/16/20
 */
public interface AddressService {

    List<DropDownResponseDTO> fetchZoneDropDown();

    List<DropDownResponseDTO> fetchProvinceDropDown();

    List<DropDownResponseDTO> fetchDistrictDropDownByZoneId(Long zoneId);

    List<DropDownResponseDTO> fetchDistrictDropDownByProvinceId(Long provinceId);

    List<DropDownResponseDTO> fetchStreetDropDownByDistrictId(Long districtId);

    List<DropDownResponseDTO> fetchMunicipalityDropDownByDistrictId(Long districtId);
}
