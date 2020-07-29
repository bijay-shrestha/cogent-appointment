package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.BillingModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.BillingModeConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.BillingModeConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.BillingModeConstants.BASE_BILLING_MODE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_BILLING_MODE)
@Api(BASE_API_VALUE)
public class BillingModeResource {

    private final BillingModeService service;

    public BillingModeResource(BillingModeService service) {
        this.service = service;
    }


    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveMinBillingMode() {
        return ok(service.fetchActiveMinBillingMode());
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchMinBillingMode() {
        return ok(service.fetchMinBillingMode());
    }
}
