package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
public interface HospitalService {

    Hospital fetchActiveHospital(Long id);

    List<AppointmentServiceTypeDropDownResponseDTO> fetchAssignedAppointmentServiceType();
}
