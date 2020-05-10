package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentDateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferTimeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.DoctorChargeRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentTransferConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentTransferConstants.*;
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

    @PutMapping(APPOINTMENT_DATE)
    @ApiOperation(FETCH_AVAILABLE_DATES)
    public ResponseEntity<?> fetchDoctorAvailableDates(@RequestBody AppointmentDateRequestDTO requestDTO){
        return ok(appointmentTransferService.fetchAvailableDatesByDoctorId(requestDTO));
    }

    @PutMapping(APPOINTMENT_TIME)
    @ApiOperation(FETCH_AVAILABLE_TIME)
    public ResponseEntity<?> fetchDoctorAvailableTime(@RequestBody AppointmentTransferTimeRequestDTO requestDTO){
        return ok(appointmentTransferService.fetchAvailableDoctorTime(requestDTO));
    }

    @PutMapping(APPOINTMENT_CHARGE)
    @ApiOperation(FETCH_DOCTOR_CHARGE)
    public ResponseEntity<?> fetchDoctorChargeByDoctorId(@RequestBody DoctorChargeRequestDTO requestDTO){
        return ok(appointmentTransferService.fetchDoctorChargeByDoctorId(requestDTO));
    }

    @PutMapping
    @ApiOperation(APPOINTMENT_TRANSFER)
    public ResponseEntity<?> appointmentTransfer(@RequestBody AppointmentTransferRequestDTO requestDTO){
        appointmentTransferService.appointmentTransfer(requestDTO);
        return ok().build();
    }
}
