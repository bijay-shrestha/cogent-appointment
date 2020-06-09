package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentStatusConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.STATUS;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.ROOM;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 16/12/2019
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentStatusResource {

    private final AppointmentStatusService appointmentStatusService;

    public AppointmentStatusResource(AppointmentStatusService appointmentStatusService) {
        this.appointmentStatusService = appointmentStatusService;
    }

    @PutMapping(STATUS)
    @ApiOperation(FETCH_APPOINTMENT_STATUS)
    public ResponseEntity<?> fetchAppointmentStatus(@Valid @RequestBody AppointmentStatusRequestDTO requestDTO) {
        return ok(appointmentStatusService.fetchAppointmentStatusResponseDTO(requestDTO));
    }

    @PutMapping(BASE_HOSPITAL_DEPARTMENT + STATUS)
    @ApiOperation(FETCH_DEPARTMENT_APPOINTMENT_STATUS)
    public ResponseEntity<?> fetchHospitalDeptAppointmentStatus(
            @Valid @RequestBody HospitalDeptAppointmentStatusRequestDTO requestDTO) {
        return ok(appointmentStatusService.fetchHospitalDeptAppointmentStatusResponseDTO(requestDTO));
    }

    @PutMapping(BASE_HOSPITAL_DEPARTMENT +ROOM+ STATUS)
    @ApiOperation(FETCH_DEPARTMENT_APPOINTMENT_STATUS_ROOM_WISE)
    public ResponseEntity<?> fetchHospitalDeptAppointmentStatusRoomwise(@Valid @RequestBody HospitalDeptAppointmentStatusRequestDTO requestDTO) {
        return ok(appointmentStatusService.fetchHospitalDeptAppointmentStatusRoomwise(requestDTO));
    }
}
