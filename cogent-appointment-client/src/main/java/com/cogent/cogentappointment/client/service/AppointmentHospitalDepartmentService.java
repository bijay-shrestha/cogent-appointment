package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment.AppointmentHospitalDepartmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentPendingApprovalResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 16/06/20
 */
public interface AppointmentHospitalDepartmentService {

    List<AppointmentHospitalDepartmentPendingApprovalResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentPendingApprovalSearchDTO searchDTO,
            Pageable pageable);

}
