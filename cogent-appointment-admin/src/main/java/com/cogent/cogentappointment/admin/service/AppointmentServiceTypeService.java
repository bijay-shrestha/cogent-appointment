package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;

import java.util.List;

/**
 * @author smriti on 26/05/20
 */
public interface AppointmentServiceTypeService {

    List<DropDownResponseDTO> fetchActiveMinInfo();

    AppointmentServiceType fetchById(Long id);
}
