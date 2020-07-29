package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.count.HospitalDeptAppointmentStatusCountRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.count.HospitalDeptAppointmentStatusCountResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAppointmentStatusDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;

import java.util.List;

/**
 * @author smriti ON 16/12/2019
 */
public interface AppointmentStatusService {

    AppointmentStatusDTO fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO);

    HospitalDeptAppointmentStatusDTO fetchHospitalDeptAppointmentStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDeptAppointmentStatusRoomwise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);

    HospitalDeptAppointmentStatusCountResponseDTO fetchHospitalDeptAppointmentStatusCount(
            HospitalDeptAppointmentStatusCountRequestDTO requestDTO);

}
