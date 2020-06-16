package com.cogent.cogentappointment.commons.service;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author Sauravi Thapa ON 6/15/20
 */

public interface AddressService {

    List<DropDownResponseDTO> fetchZoneDropDown();

}
