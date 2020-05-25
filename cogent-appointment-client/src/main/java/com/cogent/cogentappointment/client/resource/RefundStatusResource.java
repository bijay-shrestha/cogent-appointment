package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.RefundStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.RefundStatusConstants.BASE_REFUND_STATUS;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT + BASE_REFUND_STATUS)
@RestController
public class RefundStatusResource {

    private final RefundStatusService refundStatusService;

    public RefundStatusResource(RefundStatusService refundStatusService) {
        this.refundStatusService = refundStatusService;
    }
}
