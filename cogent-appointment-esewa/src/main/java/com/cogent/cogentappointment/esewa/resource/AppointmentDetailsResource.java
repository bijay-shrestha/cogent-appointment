package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentDetailsConstant.*;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentDetailsConstant.FETCH_AVAILABLE_DATES;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentDetailsConstant.FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.DOCTOR_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.EsewaConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.EsewaConstants.DOCTOR_WITH_SPECIALIZATION_AVAILABLE_DATES;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.EsewaConstants.FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.SPECIALIZATION_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentDetailsConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 4/2/20
 */

@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentDetailsResource {

    private final AppointmentDetailsService appointmentDetailsService;

    public AppointmentDetailsResource(AppointmentDetailsService appointmentDetailsService) {
        this.appointmentDetailsService = appointmentDetailsService;
    }

    @PutMapping(AVAILABLE_APPOINTMENT_DATES_AND_TIME)
    @ApiOperation(FETCH_AVAILABLE_APPOINTMENT_DATES)
    public ResponseEntity<?> fetchAvailableDatesAndTime(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(appointmentDetailsService.fetchAvailableDatesAndTime(requestDTO));
    }

    @PutMapping(FETCH_DOCTOR_AVAILABLE_STATUS)
    @ApiOperation(FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION)
    public ResponseEntity<?> fetchDoctorAvailableStatus(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(appointmentDetailsService.fetchDoctorAvailableStatus(requestDTO));
    }

    @GetMapping(DOCTOR_AVAILABLE_DATES + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_DOCTOR_DATES)
    public ResponseEntity<?> fetchAvailableDatesWithSpecialization(@PathVariable("doctorId") Long doctorId) {
        return ok(appointmentDetailsService.fetchAvailableDatesWithSpecialization(doctorId));
    }

    @GetMapping(SPECIALIZATION_AVAILABLE_DATES + SPECIALIZATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_SPECIALIZATION_DATES)
    public ResponseEntity<?> fetchAvailableDatesWithDoctor(@PathVariable("specializationId") Long specializationId) {
        return ok(appointmentDetailsService.fetchAvailableDatesWithDoctor(specializationId));
    }

    @PutMapping(DOCTOR_WITH_SPECIALIZATION_AVAILABLE_DATES)
    @ApiOperation(FETCH_AVAILABLE_DATES)
    public ResponseEntity<?> fetchAvailableDates(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(appointmentDetailsService.fetchAvailableDates(requestDTO));
    }

    @PutMapping(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION)
    @ApiOperation(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION)
    public ResponseEntity<?> fetchAvailableDoctorWithSpecialization(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(appointmentDetailsService.fetchAvailableDoctorWithSpecialization(requestDTO));
    }


}
