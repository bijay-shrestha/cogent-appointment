package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti on 14/07/20
 */
public interface AppointmentModeService {

    List<DropDownResponseDTO> fetchActiveMinAppointmentMode();
}
