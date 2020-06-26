package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;

import java.util.List;

/**
 * @author smriti on 26/05/20
 */
public interface AppointmentServiceTypeService {

    List<DropDownResponseDTO> fetchActiveMinInfo();

    AppointmentServiceType fetchActiveById(Long id);

    List<AppointmentServiceTypeDropDownResponseDTO> fetchServiceTypeNameAndCodeList();
}
