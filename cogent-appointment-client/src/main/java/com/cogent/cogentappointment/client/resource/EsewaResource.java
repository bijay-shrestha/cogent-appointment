package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.service.EsewaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.EsewaConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.EsewaConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SpecializationConstants.SPECIALIZATION_ID_PATH_VARIABLE_BASE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = API_V1 + BASE_ESEWA)
@Api(BASE_API_VALUE)
public class EsewaResource {

    private final EsewaService esewaService;

    public EsewaResource(EsewaService esewaService) {
        this.esewaService = esewaService;
    }

    @PutMapping(AVAILABLE_APPOINTMENT_DATES_AND_TIME)
    @ApiOperation(FETCH_AVAILABLE_APPOINTMENT_DATES)
    public ResponseEntity<?> fetchAvailableDatesAndTime(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDatesAndTime(requestDTO));
    }

    @PutMapping(FETCH_DOCTOR_AVAILABLE_STATUS)
    @ApiOperation(FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION)
    public ResponseEntity<?> fetchDoctorAvailableStatus(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(esewaService.fetchDoctorAvailableStatus(requestDTO));
    }

    @GetMapping(DOCTOR_AVAILABLE_DATES + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_DOCTOR_DATES)
    public ResponseEntity<?> fetchAvailableDatesWithSpecialization(@PathVariable("doctorId") Long doctorId) {
        return ok(esewaService.fetchAvailableDatesWithSpecialization(doctorId));
    }

    @GetMapping(SPECIALIZATION_AVAILABLE_DATES + SPECIALIZATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_SPECIALIZATION_DATES)
    public ResponseEntity<?> fetchAvailableDatesWithDoctor(@PathVariable("specializationId") Long specializationId) {
        return ok(esewaService.fetchAvailableDatesWithDoctor(specializationId));
    }

    @PutMapping(DOCTOR_WITH_SPECIALIZATION_AVAILABLE_DATES)
    @ApiOperation(FETCH_AVAILABLE_DATES)
    public ResponseEntity<?> fetchAvailableDates(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDates(requestDTO));
    }

    @PutMapping(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION)
    @ApiOperation(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION)
    public ResponseEntity<?> fetchAvailableDoctorWithSpecialization(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDoctorWithSpecialization(requestDTO));
    }


}
