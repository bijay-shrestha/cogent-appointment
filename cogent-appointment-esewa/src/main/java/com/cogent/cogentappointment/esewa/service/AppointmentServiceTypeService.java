package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;

import java.util.List;

/**
 * @author smriti on 26/05/20
 */
public interface AppointmentServiceTypeService {

    List<DropDownResponseDTO> fetchActiveMinInfo();
}
