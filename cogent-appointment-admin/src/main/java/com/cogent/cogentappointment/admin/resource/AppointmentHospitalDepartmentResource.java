package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.CancelledHospitalDeptAppointmentSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.service.AppointmentHospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.HospitalDepartmentAppointmentLogConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.HospitalDepartmentAppointmentLogConstant.FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENT;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DETAIL;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 6/12/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT)
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
            @RequestBody AppointmentHospitalDepartmentCheckInSearchDTO searchDTO,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentHospitalDepartmentService.searchPendingHospitalDeptAppointments(searchDTO, pageable));
    }

    @GetMapping(PENDING_APPROVAL + DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
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

    @PutMapping(REFUND)
    @ApiOperation(FETCH_CANCELLED_HOSPITAL_DEPARTMENT_APPOINTMENTS)
    public ResponseEntity<?> fetchCancelledHospitalDeptAppointments(@RequestBody CancelledHospitalDeptAppointmentSearchDTO searchDTO,
                                                                    @RequestParam("page") int page,
                                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentHospitalDepartmentService.fetchCancelledHospitalDeptAppointments(searchDTO, pageable));
    }

    @GetMapping(REFUND + DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_CANCELLED_APPOINTMENTS_DETAIL)
    public ResponseEntity<?> fetchCancelledAppointmentDetail(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentHospitalDepartmentService.fetchCancelledAppointmentDetail(appointmentId));
    }
}
