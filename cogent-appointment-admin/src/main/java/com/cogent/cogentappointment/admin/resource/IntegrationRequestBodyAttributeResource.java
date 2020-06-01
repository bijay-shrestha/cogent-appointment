package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.ApiFeatureIntegrationRequestBodyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationRequestBodyAttribute.ApiIntegrationRequestBodySearchRequestDTO;
import com.cogent.cogentappointment.admin.service.ApiRequestBodyAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.DELETE_CLIENT_INTEGRATION_FEATURE_OPERATION;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationRequestBodyConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationRequestBodyAttributeConstants.*;
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

    private final ApiRequestBodyAttributeService
            requestBodyAttributeService;

    public IntegrationRequestBodyAttributeResource
            (ApiRequestBodyAttributeService requestBodyAttributeService) {
        this.requestBodyAttributeService = requestBodyAttributeService;
    }

    @PostMapping
    @ApiOperation(SAVE_INTEGRATION_REQUEST_BODY_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ApiFeatureIntegrationRequestBodyRequestDTO requestDTO) {
        requestBodyAttributeService.save(requestDTO);
        return created(create(API_V1 + BASE_REQUEST_BODY_INTEGRATION)).build();
    }

    @PutMapping(API_REQUEST_BODY_ATTRIBUTES)
    @ApiOperation(SEARCH_API_REQUEST_BODY_ATTRIBUTE_OPERATION)
    public ResponseEntity<?> search(@RequestBody ApiIntegrationRequestBodySearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ok().body(requestBodyAttributeService.search(searchRequestDTO, pageable));
    }

    @DeleteMapping
    @ApiOperation(DELETE_API_REQUEST_HEADER_ATTRIBUTE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        requestBodyAttributeService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(FEATURE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_API_REQUEST_HEADER_ATTRIBUTE_OPERATION)
    public ResponseEntity<?> fetchRequestBodyAttributeByFeatureId(@PathVariable("featureId") Long featureId) {
        return ok(requestBodyAttributeService.fetchRequestBodyAttributeByFeatureId(featureId));
    }

}
