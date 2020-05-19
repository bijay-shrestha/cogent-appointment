package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak on 2020-05-18
 */
@RestController
@RequestMapping(API_V1 + BASE_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationResource {

    private final IntegrationService integrationService;

    public IntegrationResource(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping(FEATURES + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchFeatureTypeForDropdown() {
        return ok(integrationService.fetchActiveFeatureType());
    }

    @GetMapping(HTTP_REQUEST_METHODS + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchQualificationForDropDown() {
        return ok(integrationService.fetchActiveRequestMethod());
    }

}
