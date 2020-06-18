package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointmentHospitalDepartment.AppointmentHospitalDepartmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentHospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentConstant.APPROVE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentHospitalDepartmentConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentHospitalDepartmentConstant.FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DETAIL;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.APPOINTMENT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 16/06/20
 */
@RequestMapping(API_V1 + APPOINTMENT + BASE_HOSPITAL_DEPARTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentHospitalDepartmentResource {

    private final AppointmentHospitalDepartmentService appointmentHospitalDepartmentService;

    public AppointmentHospitalDepartmentResource(AppointmentHospitalDepartmentService appointmentHospitalDepartmentService) {
        this.appointmentHospitalDepartmentService = appointmentHospitalDepartmentService;
    }

    @PutMapping(PENDING_APPROVAL)
    @ApiOperation(FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENT)
    public ResponseEntity<?> searchPendingHospitalDeptAppointments(
            @RequestBody AppointmentHospitalDepartmentPendingApprovalSearchDTO searchDTO,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentHospitalDepartmentService.searchPendingHospitalDeptAppointments(searchDTO, pageable));
    }

    @PutMapping(PENDING_APPROVAL + DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENT)
    public ResponseEntity<?> fetchPendingHospitalDeptAppointmentDetail(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentHospitalDepartmentService.fetchPendingHospitalDeptAppointmentDetail(appointmentId));
    }

    @PutMapping(APPROVE)
    @ApiOperation(APPROVE_APPOINTMENT)
    public ResponseEntity<?> approveAppointment(@Valid @RequestBody IntegrationBackendRequestDTO integrationBackendRequestDTO) {
        appointmentHospitalDepartmentService.approveAppointment(integrationBackendRequestDTO);
        return ok().build();
    }

}
