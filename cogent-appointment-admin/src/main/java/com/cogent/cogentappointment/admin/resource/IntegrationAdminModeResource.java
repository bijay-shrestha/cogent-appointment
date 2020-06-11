package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.integrationAdminModeUpdate.AdminModeIntegrationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminModeFeatureIntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationAdminModeConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationAdminModeConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak on 2020-05-21
 */
@RestController
@RequestMapping(API_V1 + BASE_ADMIN_MODE_INTEGRATION)
@Api(SwaggerConstants.IntegrationAdminModeConstant.BASE_API_VALUE)
public class IntegrationAdminModeResource {

    private final AdminModeFeatureIntegrationService adminModeIntegrationService;

    public IntegrationAdminModeResource(AdminModeFeatureIntegrationService adminModeIntegrationService) {
        this.adminModeIntegrationService = adminModeIntegrationService;
    }

    @PostMapping
    @ApiOperation(SAVE_ADMIN_MODE_INTEGRATION_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AdminModeApiIntegrationRequestDTO requestDTO) {
        adminModeIntegrationService.save(requestDTO);
        return created(create(API_V1 + BASE_ADMIN_MODE_INTEGRATION)).build();
    }

    @PutMapping(ADMIN_MODE_INTEGRATION)
    @ApiOperation(SEARCH_ADMIN_MODE_INTEGRATION_OPERATION)
    public ResponseEntity<?> search(@RequestBody AdminModeApiIntegrationSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(adminModeIntegrationService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ADMIN_MODE_UPDATE_DETAILS + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_FOR_UPDATE_ADMIN_MODE_INTEGRATION_MODAL_OPERATION)
    public ResponseEntity<?> fetchDetailsForUpdate(@PathVariable("id") Long id) {
        return ok(adminModeIntegrationService.fetchDetailsForUpdate(id));
    }

    @PutMapping
    @ApiOperation(UPDATE_ADMIN_MODE_INTEGRATION_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody AdminModeIntegrationUpdateRequestDTO requestDTO) {
        adminModeIntegrationService.update(requestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_ADMIN_MODE_INTEGRATION_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        adminModeIntegrationService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(ADMIN_MODE_FEATURE_INTEGRATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ADMIN_MODE_INTEGRATION_DETAIL)
    public ResponseEntity<?> fetchAdminModeIntegrationById(@PathVariable("id") Long id) {
        return ok(adminModeIntegrationService.fetchAdminModeIntegrationById(id));
    }


}
