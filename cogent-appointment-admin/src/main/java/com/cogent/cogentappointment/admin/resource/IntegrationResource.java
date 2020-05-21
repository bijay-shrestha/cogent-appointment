package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.service.HttpRequestMethodService;
import com.cogent.cogentappointment.admin.service.IntegrationFeatureService;
import com.cogent.cogentappointment.admin.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.*;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.QualificationConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak on 2020-05-18
 */
@RestController
@RequestMapping(API_V1 + BASE_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationResource {

    private final IntegrationService integrationService;
    private final IntegrationFeatureService integrationFeatureService;
    private final HttpRequestMethodService httpRequestMethodService;

    public IntegrationResource(IntegrationService integrationService,
                               IntegrationFeatureService integrationFeatureService,
                               HttpRequestMethodService httpRequestMethodService) {
        this.integrationService = integrationService;
        this.integrationFeatureService = integrationFeatureService;
        this.httpRequestMethodService = httpRequestMethodService;
    }

    @PostMapping
    @ApiOperation(SAVE_CLIENT_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ClientApiIntegrationRequestDTO requestDTO) {
        integrationService.save(requestDTO);
        return created(create(API_V1 + BASE_INTEGRATION)).build();
    }

    @GetMapping(FEATURES + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchFeatureTypeForDropdown() {
        return ok(integrationFeatureService.fetchActiveFeatureType());
    }

    @GetMapping(HTTP_REQUEST_METHODS + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchQualificationForDropDown() {
        return ok(httpRequestMethodService.fetchActiveRequestMethod());
    }

}
