package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.CancelledHospitalDeptAppointmentSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.refund.CancelledHospitalDeptAppointmentResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 6/12/20
 */

public interface AppointmentHospitalDepartmentService {

    List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            Pageable pageable);

    AppointmentHospitalDepartmentCheckInDetailResponseDTO fetchPendingHospitalDeptAppointmentDetail(Long appointmentId);

    void approveAppointment(IntegrationBackendRequestDTO integrationRequestDTO);

    CancelledHospitalDeptAppointmentResponseDTO fetchCancelledHospitalDeptAppointments(CancelledHospitalDeptAppointmentSearchDTO searchDTO, Pageable pageable);
}
