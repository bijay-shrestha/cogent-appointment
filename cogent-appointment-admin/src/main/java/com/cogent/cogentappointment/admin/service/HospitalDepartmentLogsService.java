package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.HospitalDepartmentAppointmentLogResponseDTO;
import org.springframework.data.domain.Pageable;

/**
 * @author Sauravi Thapa ON 6/12/20
 */

public interface HospitalDepartmentLogsService {

    HospitalDepartmentAppointmentLogResponseDTO searchAppointmentLogs(HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO, Pageable pageable);
}
