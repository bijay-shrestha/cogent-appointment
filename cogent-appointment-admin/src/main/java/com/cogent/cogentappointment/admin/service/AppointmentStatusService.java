package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;

import java.util.List;

/**
 * @author smriti ON 16/12/2019
 */
public interface AppointmentStatusService {

    List<DoctorDutyRosterStatusResponseDTO> fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO);
}
