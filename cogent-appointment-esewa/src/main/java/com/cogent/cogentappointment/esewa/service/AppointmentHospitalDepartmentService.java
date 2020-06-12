package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.AppointmentHospitalDeptCheckAvailabilityResponseDTO;

import java.util.List;

/**
 * @author smriti on 28/05/20
 */
public interface AppointmentHospitalDepartmentService {

    List<AppointmentHospitalDeptCheckAvailabilityResponseDTO> fetchAvailableTimeSlots
            (AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO);
}
