package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.DepartmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.refund.DepartmentCancelApprovalResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 16/06/20
 */
public interface AppointmentHospitalDepartmentService {

    List<AppointmentHospitalDepartmentCheckInResponseDTO> searchPendingHospitalDeptAppointments(
            AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            Pageable pageable);

    AppointmentHospitalDepartmentCheckInDetailResponseDTO fetchPendingHospitalDeptAppointmentDetail(Long appointmentId);

    void approveAppointment(IntegrationBackendRequestDTO integrationRequestDTO);

    DepartmentCancelApprovalResponse fetchDepartmentAppointmentCancelApprovals(DepartmentCancelApprovalSearchDTO searchDTO,
                                                                               Pageable pageable);
}
