package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RefundStatusConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RefundStatusConstant.FETCH_PENDING_REFUND_APPROVAL_LIST;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RefundStatusConstants.BASE_REFUND_STATUS;
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

    @PutMapping
    @ApiOperation(FETCH_PENDING_REFUND_APPROVAL_LIST)
    public ResponseEntity<?> fetchRefundAppointments(@RequestBody RefundStatusSearchRequestDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(refundStatusService.searchRefundAppointments(searchDTO, pageable));
    }
}
