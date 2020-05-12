package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.SalutationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SalutationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SalutationConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SalutationConstant.BASE_SALUTATION;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(API_V1 + BASE_SALUTATION)
@Api(BASE_API_VALUE)
public class SalutationResource {

    private final SalutationService salutationService;

    public SalutationResource(SalutationService salutationService) {
        this.salutationService = salutationService;
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchSalutationForDropDown() {
        return ok(salutationService.fetchActiveMinSalutation());
    }
}
