package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DETAIL;
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

    /*eSewa*/
    @PutMapping(CHECK_AVAILABILITY)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.checkAvailability(requestDTO));
    }

    /*eSewa*/
    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.save(requestDTO));
    }

    /*eSewa*/
    @PutMapping(PENDING_APPOINTMENT)
    @ApiOperation((FETCH_PENDING_APPOINTMENT))
    public ResponseEntity<?> fetchPendingAppointments(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchPendingAppointments(searchDTO));
    }

    /*eSewa*/
    @PutMapping(CANCEL)
    @ApiOperation(CANCEL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
        appointmentService.cancelAppointment(cancelRequestDTO);
        return ok().build();
    }

    /*eSewa*/
    @PutMapping(RESCHEDULE)
    @ApiOperation(RESCHEDULE_OPERATION)
    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
        appointmentService.rescheduleAppointment(requestDTO);
        return ok().build();
    }

    /*eSewa*/
    @GetMapping(DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    public ResponseEntity<?> fetchAppointmentDetails(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchAppointmentDetails(appointmentId));
    }

    /*eSewa*/
    @PutMapping(HISTORY)
    @ApiOperation(FETCH_APPOINTMENT_HISTORY)
    public ResponseEntity<?> fetchAppointmentHistory(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchAppointmentHistory(searchDTO));
    }

    /*eSewa*/
    @GetMapping(CANCEL + APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(CANCEL_REGISTRATION_OPERATION)
    public ResponseEntity<?> cancelRegistration(@PathVariable("appointmentReservationId") Long appointmentReservationId) {
        appointmentService.cancelRegistration(appointmentReservationId);
        return ok().build();
    }

    @PutMapping(REFUND)
    @ApiOperation(FETCH_REFUND_APPOINTMENTS)
    public ResponseEntity<?> fetchRefundAppointments(@RequestBody AppointmentRefundSearchDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchRefundAppointments(searchDTO, pageable));
    }

    @GetMapping(REFUND + APPROVE + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(APPROVE_REFUND_APPOINTMENT)
    public ResponseEntity<?> approveRefundAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.approveRefundAppointment(appointmentId);
        return ok().build();
    }

    @PutMapping(REFUND + REJECT)
    @ApiOperation(REJECT_REFUND_APPOINTMENT)
    public ResponseEntity<?> rejectRefundAppointment(@Valid @RequestBody AppointmentRefundRejectDTO refundRejectDTO) {
        appointmentService.rejectRefundAppointment(refundRejectDTO);
        return ok().build();
    }

}
