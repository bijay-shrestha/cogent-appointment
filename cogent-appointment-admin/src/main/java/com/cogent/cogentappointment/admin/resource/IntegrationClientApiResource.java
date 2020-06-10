package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.ClientApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationClient.clientIntegrationUpdate.ClientApiIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationClientConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(API_V1 + BASE_CLIENT_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationClientApiResource {

    private final IntegrationService integrationService;

    public IntegrationClientApiResource(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @PostMapping
    @ApiOperation(SAVE_CLIENT_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ClientApiIntegrationRequestDTO requestDTO) {
        integrationService.save(requestDTO);
        return created(create(API_V1 + BASE_CLIENT_INTEGRATION)).build();
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
    public ResponseEntity<?> fetchClientApiIntegrationById(@PathVariable("id") Long id) {
        return ok(integrationService.fetchClientApiIntegrationById(id));
    }

    @DeleteMapping
    @ApiOperation(DELETE_CLIENT_INTEGRATION_FEATURE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        integrationService.delete(deleteRequestDTO);
        return ok().build();
    }
}
