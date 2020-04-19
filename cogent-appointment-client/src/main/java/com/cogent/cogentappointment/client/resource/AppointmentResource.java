package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.*;
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

    /*esewa*/
    @PutMapping(FETCH_AVAILABLE_TIMESLOTS)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> fetchAvailableTimeSlots(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.fetchAvailableTimeSlots(requestDTO));
    }

    //todo: shift in esewa-module
    /*esewa*/
    @PutMapping(FETCH_CURRENT_AVAILABLE_TIMESLOTS)
    @ApiOperation(CHECK_CURRENT_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> fetchCurrentAvailableTimeSlots(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.fetchCurrentAvailableTimeSlots(requestDTO));
    }

    /*esewa*/
    @PostMapping(SELF)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> saveAppointmentForSelf(@Valid @RequestBody AppointmentRequestDTOForSelf requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.saveAppointmentForSelf(requestDTO));
    }

    /*esewa*/
    @PostMapping(OTHERS)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> saveAppointmentForOthers(@Valid @RequestBody AppointmentRequestDTOForOthers requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.saveAppointmentForOthers(requestDTO));
    }

    /*esewa*/
    @PutMapping(PENDING_APPOINTMENT)
    @ApiOperation((FETCH_PENDING_APPOINTMENT))
    public ResponseEntity<?> fetchPendingAppointments(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchPendingAppointments(searchDTO));
    }

    /*esewa*/
    @PutMapping(CANCEL)
    @ApiOperation(CANCEL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
        return ok(appointmentService.cancelAppointment(cancelRequestDTO));
    }

    /*esewa*/
    @PutMapping(RESCHEDULE)
    @ApiOperation(RESCHEDULE_OPERATION)
    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
        return ok(appointmentService.rescheduleAppointment(requestDTO));
    }

    /*esewa*/
    @GetMapping(DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    public ResponseEntity<?> fetchAppointmentDetails(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchAppointmentDetails(appointmentId));
    }

    /*esewa*/
    @PutMapping(HISTORY)
    @ApiOperation(FETCH_APPOINTMENT_HISTORY)
    public ResponseEntity<?> fetchAppointmentHistory(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchAppointmentHistory(searchDTO));
    }

    /*esewa*/
    @GetMapping(CANCEL + APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(CANCEL_REGISTRATION_OPERATION)
    public ResponseEntity<?> cancelRegistration(@PathVariable("appointmentReservationId") Long appointmentReservationId) {
        return ok(appointmentService.cancelRegistration(appointmentReservationId));
    }

    @PutMapping(TRANSACTION_STATUS)
    @ApiOperation(FETCH_APPOINTMENT_TRANSACTION_STATUS)
    public ResponseEntity<?> fetchAppointmentTransactionStatus(
            @Valid @RequestBody AppointmentTransactionStatusRequestDTO requestDTO) {
        return ok(appointmentService.fetchAppointmentTransactionStatus(requestDTO));
    }

    @PutMapping(PENDING_APPROVAL)
    @ApiOperation(FETCH_PENDING_APPOINTMENT_APPROVAL)
    public ResponseEntity<?> search(@RequestBody AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.searchPendingVisitApprovals(searchRequestDTO, pageable));
    }

    @GetMapping(PENDING_APPROVAL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_PENDING_APPOINTMENT_APPROVAL_DETAIL)
    public ResponseEntity<?> fetchDetailByAppointmentId(@PathVariable("appointmentId") Long appointmentId) {
        return ok(appointmentService.fetchDetailByAppointmentId(appointmentId));
    }

    @GetMapping(APPROVE + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(APPROVE_APPOINTMENT)
    public ResponseEntity<?> approveAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.approveAppointment(appointmentId);
        return ok().build();
    }

    @PutMapping(REJECT)
    @ApiOperation(REJECT_APPOINTMENT)
    public ResponseEntity<?> rejectAppointment(@Valid @RequestBody AppointmentRejectDTO rejectDTO) {
        appointmentService.rejectAppointment(rejectDTO);
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

    @GetMapping(REFUND + DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_REFUND_APPOINTMENTS_DETAIL)
    public ResponseEntity<?> fetchRefundDetailsById(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchRefundDetailsById(appointmentId));
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

    @PutMapping(LOG)
    @ApiOperation(FETCH_APPOINTMENT_LOG)
    public ResponseEntity<?> fetchAppointmentLog(@RequestBody AppointmentLogSearchDTO searchRequestDTO,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.searchAppointmentLogs(searchRequestDTO, pageable));
    }

    @PutMapping(RESCHEDULE_LOG)
    @ApiOperation(FETCH_APPOINTMENT_RESCHEDULE_LOG)
    public ResponseEntity<?> fetchAppointmentLog(@RequestBody AppointmentRescheduleLogSearchDTO rescheduleLogSearchDTO,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchRescheduleAppointment(rescheduleLogSearchDTO, pageable));
    }


}
