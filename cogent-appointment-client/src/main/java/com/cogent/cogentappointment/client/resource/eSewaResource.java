package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.service.EsewaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.EsewaConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.EsewaConstants.*;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = API_V1 + BASE_ESEWA)
@Api(BASE_API_VALUE)
public class eSewaResource {

    private final EsewaService esewaService;

    public eSewaResource(EsewaService esewaService) {
        this.esewaService = esewaService;
    }

    @PutMapping(APPOINTMENT_AVAILABLE_DATES)
    @ApiOperation(FETCH_AVAILABLE_APPOINTMENT_DATES)
    public ResponseEntity<?> doctorAvailableTime(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.fetchDoctorAvailableDatesAndTime(requestDTO));
    }

    @PutMapping(FETCH_DOCTOR_AVAILABLE_STATUS)
    @ApiOperation(FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION)
    public ResponseEntity<?> fetchDoctorAvailableStatus(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(esewaService.fetchDoctorAvailableStatus(requestDTO));
    }

    @GetMapping(AVAILABLE_DOCTOR_DATES + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION)
    public ResponseEntity<?> avaliableDoctorDates(@PathVariable("doctorId") Long doctorId) {
        return ok(esewaService.fetchDoctorAvailableDates(doctorId));
    }


}
