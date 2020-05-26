package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.clientIntegration.clientIntegrationUpdate.ClientApiIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.ApiIntegrationTypeService;
import com.cogent.cogentappointment.admin.service.HttpRequestMethodService;
import com.cogent.cogentappointment.admin.service.IntegrationFeatureService;
import com.cogent.cogentappointment.admin.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ApiIntegrationTypeService apiIntegrationTypeService;

    public IntegrationResource(IntegrationService integrationService,
                               IntegrationFeatureService integrationFeatureService,
                               HttpRequestMethodService httpRequestMethodService, ApiIntegrationTypeService apiIntegrationTypeService) {
        this.integrationService = integrationService;
        this.integrationFeatureService = integrationFeatureService;
        this.httpRequestMethodService = httpRequestMethodService;
        this.apiIntegrationTypeService = apiIntegrationTypeService;
    }

    @PostMapping
    @ApiOperation(SAVE_CLIENT_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ClientApiIntegrationRequestDTO requestDTO) {
        integrationService.save(requestDTO);
        return created(create(API_V1 + BASE_INTEGRATION)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_CLIENT_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody ClientApiIntegrationUpdateRequestDTO requestDTO) {
        integrationService.update(requestDTO);
        return ok().build();
    }

    @PutMapping(CLIENT_INTEGRATION)
    @ApiOperation(SEARCH_CLIENT_API_INTEGRATION_OPERATION)
    public ResponseEntity<?> search(@RequestBody ClientApiIntegrationSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(integrationService.search(searchRequestDTO, pageable));
    }

    @GetMapping(CLIENT_INTEGRATION_UPDATE_DETAILS + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_FOR_UPDATE_CLIENT_INTEGRATION_MODAL_OPERATION)
    public ResponseEntity<?> fetchDetailsForUpdate(@PathVariable("id") Long id) {
        return ok(integrationService.fetchDetailsForUpdate(id));
    }

    @GetMapping(CLIENT_FEATURE_INTEGRATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_CLIENT_API_INTEGRATION_DETAIL)
    public ResponseEntity<?> fetchDetailByAppointmentId(@PathVariable("id") Long id) {
        return ok(integrationService.fetchClientApiIntegrationById(id));
    }

    @DeleteMapping
    @ApiOperation(DELETE_CLIENT_INTEGRATION_FEATURE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        integrationService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(FEATURES + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchFeatureTypeForDropdown() {
        return ok(integrationFeatureService.fetchActiveFeatureType());
    }

    @GetMapping(FEATURES + CLIENT_INTEGRATION_TYPE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchFeatureTypeByIntegrationType(@PathVariable("apiIntegrationTypeId") Long id) {
        return ok(apiIntegrationTypeService.fetchActiveFeatureTypeByIntegrationTypeId(id));
    }

    @GetMapping(HTTP_REQUEST_METHODS + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchQualificationForDropDown() {
        return ok(httpRequestMethodService.fetchActiveRequestMethod());
    }

    @GetMapping(API_INTEGRATION_TYPE + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchApiIntegrationTypeForDropDown() {
        return ok(apiIntegrationTypeService.fetchActiveApiIntegrationType());
    }

}
