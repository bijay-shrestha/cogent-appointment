package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
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

    @PutMapping(REFUND)
    @ApiOperation(FETCH_REFUND_APPOINTMENTS)
    public ResponseEntity<?> fetchRefundAppointments(@RequestBody AppointmentRefundSearchDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchRefundAppointments(searchDTO, pageable));
    }

    @GetMapping(REFUND + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(REJECT_REFUND_APPOINTMENT)
    public ResponseEntity<?> approveRefundAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.approveRefundAppointment(appointmentId);
        return ok().build();
    }

    @PutMapping(REFUND + REJECT)
    @ApiOperation(REJECT_REFUND_APPOINTMENT)
    public ResponseEntity<?> rejectRefundAppointment(@RequestBody AppointmentRefundRejectDTO refundRejectDTO) {
        appointmentService.rejectRefundAppointment(refundRejectDTO);
        return ok().build();
    }
}
