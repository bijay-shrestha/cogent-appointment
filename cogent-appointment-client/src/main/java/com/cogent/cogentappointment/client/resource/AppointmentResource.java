package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
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

    @PutMapping(CHECK_AVAILABILITY)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.checkAvailability(requestDTO));
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.save(requestDTO));
    }

    @PutMapping(PENDING_APPOINTMENT)
    @ApiOperation((FETCH_PENDING_APPOINTMENT))
    public ResponseEntity<?> fetchPendingAppointments(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchPendingAppointments(searchDTO));
    }

    @PutMapping(CANCEL_APPOINTMENT)
    @ApiOperation(CANCEL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
        appointmentService.cancelAppointment(cancelRequestDTO);
        return ok().build();
    }

    @PutMapping(RESCHEDULE)
    @ApiOperation(RESCHEDULE_OPERATION)
    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
        appointmentService.rescheduleAppointment(requestDTO);
        return ok().build();
    }

    @GetMapping(DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    public ResponseEntity<?> fetchAppointmentDetails(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchAppointmentDetails(appointmentId));
    }

    @PutMapping(HISTORY)
    @ApiOperation((FETCH_APPOINTMENT_HISTORY))
    public ResponseEntity<?> fetchAppointmentHistory(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.fetchAppointmentHistory(searchDTO));
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

    @PutMapping(PENDING_APPROVAL)
    @ApiOperation(FETCH_PENDING_APPOINTMENT_APPROVAL)
    public ResponseEntity<?> search(@RequestBody AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.searchPendingVisitApprovals(searchRequestDTO, pageable));
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
