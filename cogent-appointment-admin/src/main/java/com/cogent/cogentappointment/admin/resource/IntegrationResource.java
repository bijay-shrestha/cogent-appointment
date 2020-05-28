package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.service.ApiIntegrationTypeService;
import com.cogent.cogentappointment.admin.service.HttpRequestMethodService;
import com.cogent.cogentappointment.admin.service.IntegrationFeatureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
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

    private final IntegrationFeatureService integrationFeatureService;
    private final HttpRequestMethodService httpRequestMethodService;
    private final ApiIntegrationTypeService apiIntegrationTypeService;

    public IntegrationResource(IntegrationFeatureService integrationFeatureService,
                               HttpRequestMethodService httpRequestMethodService,
                               ApiIntegrationTypeService apiIntegrationTypeService) {
        this.integrationFeatureService = integrationFeatureService;
        this.httpRequestMethodService = httpRequestMethodService;
        this.apiIntegrationTypeService = apiIntegrationTypeService;
    }


    @GetMapping(FEATURES + ACTIVE + MIN)
    @ApiOperation(SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchFeatureTypeForDropdown() {
        return ok(integrationFeatureService.fetchActiveFeatureType());
    }

    @GetMapping(FEATURES + CLIENT_INTEGRATION_TYPE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchFeatureTypeByIntegrationType(@PathVariable("apiIntegrationTypeId") Long id) {
        return ok(apiIntegrationTypeService.fetchActiveFeatureTypeByIntegrationTypeId(id));
    }

    @GetMapping(HTTP_REQUEST_METHODS + ACTIVE + MIN)
    @ApiOperation(SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchQualificationForDropDown() {
        return ok(httpRequestMethodService.fetchActiveRequestMethod());
    }

    @GetMapping(API_INTEGRATION_TYPE + ACTIVE + MIN)
    @ApiOperation(SwaggerConstants.IntegrationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchApiIntegrationTypeForDropDown() {
        return ok(apiIntegrationTypeService.fetchActiveApiIntegrationType());
    }

    @GetMapping(INTEGRATION_CHANNEL + ACTIVE + MIN)
    @ApiOperation(SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchApiIntegrationChannelForDropDown() {
        return ok(integrationFeatureService.fetchActiveApiIntegrationChannel());
    }

}
