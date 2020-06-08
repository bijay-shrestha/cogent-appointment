package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusDTO;

/**
 * @author smriti ON 16/12/2019
 */
public interface AppointmentStatusService {

    AppointmentStatusDTO fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO);

    HospitalDeptAppointmentStatusDTO fetchHospitalDeptAppointmentStatusResponseDTO(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);
}
