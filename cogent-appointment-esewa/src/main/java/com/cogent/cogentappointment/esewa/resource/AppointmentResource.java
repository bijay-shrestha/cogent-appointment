package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentResource {

    private final AppointmentService appointmentService;

    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @PutMapping(CHECK_AVAILABILITY)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.checkAvailability(requestDTO));
    }

    @PostMapping(SELF)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> saveAppointmentForSelf(@Valid @RequestBody AppointmentRequestDTOForSelf requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.saveAppointmentForSelf(requestDTO));
    }

    @PostMapping(OTHERS)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> saveAppointmentForOthers(@Valid @RequestBody AppointmentRequestDTOForOthers requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.saveAppointmentForOthers(requestDTO));
    }

    @PutMapping(PENDING_APPOINTMENT)
    @ApiOperation((FETCH_PENDING_APPOINTMENT))
    public ResponseEntity<?> fetchPendingAppointments(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchPendingAppointments(searchDTO));
    }


    @PutMapping(CANCEL)
    @ApiOperation(CANCEL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
        return ok(appointmentService.cancelAppointment(cancelRequestDTO));
    }

    @PutMapping(RESCHEDULE)
    @ApiOperation(RESCHEDULE_OPERATION)
    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
        return ok( appointmentService.rescheduleAppointment(requestDTO));
    }

    @GetMapping(DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    public ResponseEntity<?> fetchAppointmentDetails(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchAppointmentDetails(appointmentId));
    }

    @PutMapping(HISTORY)
    @ApiOperation(FETCH_APPOINTMENT_HISTORY)
    public ResponseEntity<?> fetchAppointmentHistory(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchAppointmentHistory(searchDTO));
    }

    @GetMapping(CANCEL + APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(CANCEL_REGISTRATION_OPERATION)
    public ResponseEntity<?> cancelRegistration(@PathVariable("appointmentReservationId") Long appointmentReservationId) {
        return ok(appointmentService.cancelRegistration(appointmentReservationId));
    }

}
