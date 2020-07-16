package com.cogent.cogentappointment.commons.repository.custom;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Sauravi Thapa ON 6/15/20
 */

@Repository
@Qualifier("addressRepositoryCustom")
public interface AddressRepositoryCustom {

    List<DropDownResponseDTO> getListOfZone();

    List<DropDownResponseDTO> getListOfProvince();

    List<DropDownResponseDTO> getListOfDistrictByZoneId(Long zoneId);

    List<DropDownResponseDTO> getListOfDistrictByProvinceId(Long provinceId);

    List<DropDownResponseDTO> getListOfStreetByDistrictId(Long districtId);

    List<DropDownResponseDTO> getListOfMunicipalityByDistrictId(Long districtId);
}
