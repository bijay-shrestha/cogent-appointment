package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPatientDetail.AppointmentPatientDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.service.AppointmentStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentStatusConstant.FETCH_APPOINTMENT_STATUS;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 16/12/2019
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT )
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

    @PutMapping(PATIENT_DETAIL)
    @ApiOperation(FETCH_APPOINTMENT_STATUS)
    public ResponseEntity<?> fetchPatientDetailByAppointmentTime(@Valid @RequestBody AppointmentPatientDetailRequestDTO requestDTO) {
        return ok(appointmentStatusService.fetchPatientDetailByAppointmentTime(requestDTO));
    }
}
