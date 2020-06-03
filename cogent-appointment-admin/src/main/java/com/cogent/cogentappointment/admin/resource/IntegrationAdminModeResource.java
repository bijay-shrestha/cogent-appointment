package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integrationAdminMode.AdminModeApiIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminModeFeatureIntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationAdminModeConstant.SAVE_ADMIN_MODE_INTEGRATION_OPERATION;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationAdminModeConstant.SEARCH_ADMIN_MODE_INTEGRATION_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationAdminModeConstants.ADMIN_MODE_INTEGRATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationAdminModeConstants.BASE_ADMIN_MODE_INTEGRATION;
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


}
