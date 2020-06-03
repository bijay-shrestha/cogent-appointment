package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public interface BillingModeService {

    List<DropDownResponseDTO> fetchActiveMinBillingMode();

    List<DropDownResponseDTO> fetchMinBillingMode();

}
