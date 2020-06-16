package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author Sauravi Thapa ON 6/16/20
 */
public interface AddressService {

    List<DropDownResponseDTO> fetchZoneDropDown();
}
