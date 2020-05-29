package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.integration.ApiFeatureIntegrationRequestBodyRequestDTO;
import com.cogent.cogentappointment.admin.service.ApiFeatureIntegrationRequestBodyParametersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ClientIntegrationConstant.DETAILS_FOR_UPDATE_CLIENT_INTEGRATION_MODAL_OPERATION;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationRequestBodyConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationRequestBodyConstant.FETCH_API_REQUEST_HEADER_ATTRIBUTE_OPERATION;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationRequestBodyConstant.SAVE_INTEGRATION_REQUEST_BODY_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ClientIntegrationConstants.CLIENT_FEATURE_INTEGRATION_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationRequestBodyAttributeConstants.BASE_REQUEST_BODY_INTEGRATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationRequestBodyAttributeConstants.FEATURE_ID_PATH_VARIABLE_BASE;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak ON 2020/05/29-12:33 PM
 */
@RestController
@RequestMapping(API_V1 + BASE_REQUEST_BODY_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationRequestBodyAttributeResource {

    private final ApiFeatureIntegrationRequestBodyParametersService
            featureRequestBodyParametersService;

    public IntegrationRequestBodyAttributeResource(ApiFeatureIntegrationRequestBodyParametersService featureRequestBodyParametersService) {
        this.featureRequestBodyParametersService = featureRequestBodyParametersService;
    }

    @PostMapping
    @ApiOperation(SAVE_INTEGRATION_REQUEST_BODY_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ApiFeatureIntegrationRequestBodyRequestDTO requestDTO) {
        featureRequestBodyParametersService.save(requestDTO);
        return created(create(API_V1 + BASE_REQUEST_BODY_INTEGRATION)).build();
    }

    @GetMapping(FEATURE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_API_REQUEST_HEADER_ATTRIBUTE_OPERATION)
    public ResponseEntity<?> fetchRequestBodyAttributeByFeatureId(@PathVariable("featureId") Long featureId) {
        return ok(featureRequestBodyParametersService.fetchRequestBodyAttributeByFeatureId(featureId));
    }

}
