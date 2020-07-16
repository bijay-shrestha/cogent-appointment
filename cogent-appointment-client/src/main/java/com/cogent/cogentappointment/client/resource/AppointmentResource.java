package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integrationClient.ApiIntegrationCheckInRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.service.AppointmentService;
import com.cogent.cogentappointment.client.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.IntegrationConstant.FETCH_CLIENT_API_INTEGRATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DETAIL;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.IntegrationConstants.CLIENT_API_INTEGRATION_APPOINTMENT_APPROVE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentResource {

    private final AppointmentService appointmentService;
    private final IntegrationService integrationService;

    public AppointmentResource(AppointmentService appointmentService, IntegrationService integrationService) {
        this.appointmentService = appointmentService;
        this.integrationService = integrationService;
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

    @PutMapping(APPROVE)
    @ApiOperation(APPROVE_APPOINTMENT)
    public ResponseEntity<?> approveAppointment(@RequestBody IntegrationBackendRequestDTO integrationBackendRequestDTO) {
        appointmentService.approveAppointment(integrationBackendRequestDTO);
        return ok().build();
    }

    @PutMapping(REJECT)
    @ApiOperation(REJECT_APPOINTMENT)
    public ResponseEntity<?> rejectAppointment(@Valid @RequestBody AppointmentRejectDTO rejectDTO,
                                               @RequestBody IntegrationBackendRequestDTO integrationBackendRequestDTO) {
        appointmentService.rejectAppointment(rejectDTO);
        return ok().build();
    }

    @PutMapping(REFUND)
    @ApiOperation(FETCH_APPOINTMENT_CANCEL_APPROVALS)
    public ResponseEntity<?> fetchRefundAppointments(@RequestBody AppointmentCancelApprovalSearchDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchAppointmentCancelApprovals(searchDTO, pageable));
    }

    @GetMapping(REFUND + DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_REFUND_APPOINTMENTS_DETAIL)
    public ResponseEntity<?> fetchRefundDetailsById(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchRefundDetailsById(appointmentId));
    }

    @PutMapping(REFUND + APPROVE )
    @ApiOperation(APPROVE_REFUND_APPOINTMENT)
    public ResponseEntity<?> approveRefundAppointment(@RequestBody IntegrationRefundRequestDTO refundRequestDTO) {
        appointmentService.approveRefundAppointment(refundRequestDTO);
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

    @PutMapping(TRANSACTION_LOG)
    @ApiOperation(FETCH_TRANSACTION_LOG)
    public ResponseEntity<?> fetchTransactionLog(@RequestBody TransactionLogSearchDTO searchRequestDTO,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.searchTransactionLogs(searchRequestDTO, pageable));
    }

    @PutMapping(RESCHEDULE_LOG)
    @ApiOperation(FETCH_APPOINTMENT_RESCHEDULE_LOG)
    public ResponseEntity<?> fetchAppointmentLog(@RequestBody AppointmentRescheduleLogSearchDTO rescheduleLogSearchDTO,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchRescheduleAppointment(rescheduleLogSearchDTO, pageable));
    }

    @PutMapping(CLIENT_API_INTEGRATION_APPOINTMENT_APPROVE)
    @ApiOperation(FETCH_CLIENT_API_INTEGRATION)
    public ResponseEntity<?> approveAppointmentCheckIn(@Valid @RequestBody ApiIntegrationCheckInRequestDTO requestDTO) {
        integrationService.approveAppointmentCheckIn(requestDTO);
        return ok().build();
    }

//    @PutMapping(INTEGRATION + REFUND + APPROVE)
//    @ApiOperation(APPROVE_REFUND_BY_CLIENT_INTEGRATION)
//    public ResponseEntity<?> approveRefund(@Valid @RequestBody ApiIntegrationApproveRefundRequestDTO requestDTO) {
//        integrationService.approveRefund(requestDTO);
//        return ok().build();
//    }
//
//    @PutMapping(INTEGRATION + REFUND + REJECT)
//    @ApiOperation(APPROVE_REFUND_BY_CLIENT_INTEGRATION)
//    public ResponseEntity<?> rejectRefund(@Valid @RequestBody com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationApproveRejectRequestDTO requestDTO) {
//        integrationService.rejectRefund(requestDTO);
//        return ok().build();
//    }


}
