package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminFeatureConstant.UPDATE_OPERATION;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentTransferConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentTransferConstant.FETCH_AVAILABLE_DATES;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentTransferConstant.FETCH_AVAILABLE_TIME;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentTransferConstants.APPOINTMENT_TIME;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentTransferConstants.BASE_APPOINTMENT_TRANSFER;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DOCTOR_ID_PATH_VARIABLE_BASE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/6/20
 */

@RestController
@RequestMapping(API_V1+BASE_APPOINTMENT_TRANSFER)
@Api(BASE_API_VALUE)
public class AppointmentTransferResource  {

    private final AppointmentTransferService appointmentTransferService;

    public AppointmentTransferResource(AppointmentTransferService appointmentTransferService) {
        this.appointmentTransferService = appointmentTransferService;
    }

    @GetMapping(DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_DATES)
    public ResponseEntity<?> fetchDoctorAvailableDates(@PathVariable("doctorId") Long doctorId){
        return ok(appointmentTransferService.fetchAvailableDatesByDoctorId(doctorId));
    }

    @PutMapping(APPOINTMENT_TIME)
    @ApiOperation(FETCH_AVAILABLE_TIME)
    public ResponseEntity<?> fetchDoctorAvailableTime(@RequestBody AppointmentTransferTimeRequestDTO requestDTO){
        return ok(appointmentTransferService.fetchAvailableDoctorTime(requestDTO));
    }
}
