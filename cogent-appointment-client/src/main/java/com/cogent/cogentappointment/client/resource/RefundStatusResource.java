package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RefundStatusConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.APPOINTMENT_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RefundStatusConstants.BASE_REFUND_STATUS;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RefundStatusConstants.CHECK;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RoomConstants.HOSPITAL_DEPARTMENT_WISE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT + BASE_REFUND_STATUS)
@RestController
@Api(BASE_API_VALUE)
public class RefundStatusResource {

    private final RefundStatusService refundStatusService;

    public RefundStatusResource(RefundStatusService refundStatusService) {
        this.refundStatusService = refundStatusService;
    }

    @PutMapping(SEARCH)
    @ApiOperation(FETCH_APPOINTMENT_REFUND_DETAIL_LIST)
    public ResponseEntity<?> fetchRefundAppointments(@RequestBody RefundStatusSearchRequestDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(refundStatusService.searchRefundAppointments(searchDTO, pageable));
    }

    @PutMapping(HOSPITAL_DEPARTMENT_WISE + SEARCH)
    @ApiOperation(FETCH_APPOINTMENT_REFUND_DETAIL_LIST)
    public ResponseEntity<?> fetchHospitalDepartmentRefundAppointments(@RequestBody RefundStatusSearchRequestDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(refundStatusService.searchHospitalDepartmentRefundAppointments(searchDTO, pageable));
    }

    @PutMapping(CHECK)
    @ApiOperation(FETCH_REFUND_DETAILS_TO_APPROVE)
    public ResponseEntity<?> checkRefundStatus(@Valid @RequestBody RefundStatusRequestDTO requestDTO) {
        refundStatusService.checkRefundStatus(requestDTO);
        return ok().build();
    }

    @GetMapping(DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_REFUND_STATUS_APPOINTMENTS_DETAIL)
    public ResponseEntity<?> fetchRefundDetailsById(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(refundStatusService.fetchRefundDetailsById(appointmentId));
    }

}
