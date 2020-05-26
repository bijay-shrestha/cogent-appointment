package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.DummyMessage;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import com.cogent.cogentappointment.client.utils.resttempalte.RestTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminConstant.SAVE_OPERATION;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RefundStatusConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RefundStatusConstant.FETCH_PENDING_REFUND_APPROVAL_LIST;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.RefundStatusConstant.FETCH_REFUND_DETAILS_TO_APPROVE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.APPROVE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RefundStatusConstants.BASE_REFUND_STATUS;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RefundStatusConstants.CHECK;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SEARCH;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestHeaders.getEsewaPaymentStatusAPIHeaders;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestURI.BHERI_HOSPITAL_POST_TICKET;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT + BASE_REFUND_STATUS)
@RestController
@Api(BASE_API_VALUE)
public class RefundStatusResource {

    private final RefundStatusService refundStatusService;

    private final RestTemplateUtils restTemplateUtils;

    public RefundStatusResource(RefundStatusService refundStatusService,
                                RestTemplateUtils restTemplateUtils) {
        this.refundStatusService = refundStatusService;
        this.restTemplateUtils = restTemplateUtils;
    }

    @PutMapping(SEARCH)
    @ApiOperation(FETCH_PENDING_REFUND_APPROVAL_LIST)
    public ResponseEntity<?> fetchRefundAppointments(@RequestBody RefundStatusSearchRequestDTO searchDTO,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(refundStatusService.searchRefundAppointments(searchDTO, pageable));
    }

    @PutMapping(CHECK)
    @ApiOperation(FETCH_REFUND_DETAILS_TO_APPROVE)
    public ResponseEntity<?> checkRefundStatus(@Valid @RequestBody RefundStatusRequestDTO requestDTO) {
        refundStatusService.checkRefundStatus(requestDTO);
        return ok().build();
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> paymentStatus(@RequestBody EsewaPayementStatus esewaPayementStatus) throws IOException {

        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, getEsewaPaymentStatusAPIHeaders());

        ResponseEntity<DummyMessage> response = (ResponseEntity<DummyMessage>) restTemplateUtils
                .postRequest(BHERI_HOSPITAL_POST_TICKET, request);

        System.out.println(response.getBody().getError_message());

        return ok().build();
    }
}
