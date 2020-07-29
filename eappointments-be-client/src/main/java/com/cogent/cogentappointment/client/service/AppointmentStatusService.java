package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.count.HospitalDeptAppointmentStatusCountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.count.HospitalDeptAppointmentStatusCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;

import java.util.List;

/**
 * @author smriti ON 16/12/2019
 */
public interface AppointmentStatusService {

    AppointmentStatusDTO fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO);

    HospitalDeptAppointmentStatusDTO fetchHospitalDeptAppointmentStatusResponseDTO(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDeptAppointmentStatusRoomwise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    HospitalDeptAppointmentStatusCountResponseDTO fetchHospitalDeptAppointmentStatusCount(
            HospitalDeptAppointmentStatusCountRequestDTO requestDTO);
}
