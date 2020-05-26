package com.cogent.cogentappointment.esewa.resource;


import com.cogent.cogentappointment.esewa.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.esewa.service.RefundStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.RefundStatusConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.RefundStatusConstant.FETCH_REFUND_DETAILS_TO_APPROVE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.RefundStatusConstants.APPROVE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.RefundStatusConstants.BASE_REFUND_STATUS;
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

    @PutMapping(APPROVE)
    @ApiOperation(FETCH_REFUND_DETAILS_TO_APPROVE)
    public ResponseEntity<?> fetchRefundAppointments(@Valid @RequestBody RefundStatusRequestDTO requestDTO) {
        refundStatusService.approveAppointmentRefund(requestDTO);
        return ok().build();
    }
}
