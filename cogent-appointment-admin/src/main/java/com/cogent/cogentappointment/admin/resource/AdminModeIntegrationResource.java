package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.adminModeIntegration.AdminModeFeatureIntegrationRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminModeFeatureIntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.SAVE_ADMIN_MODE_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AdminModeIntegrationConstants.BASE_ADMIN_MODE_INTEGRATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationConstants.BASE_INTEGRATION;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;

/**
 * @author rupak on 2020-05-21
 */
@RestController
@RequestMapping(API_V1 + BASE_ADMIN_MODE_INTEGRATION)
@Api(BASE_API_VALUE)
public class AdminModeIntegrationResource {

    private final AdminModeFeatureIntegrationService adminModeIntegrationService;

    public AdminModeIntegrationResource(AdminModeFeatureIntegrationService adminModeIntegrationService) {
        this.adminModeIntegrationService = adminModeIntegrationService;
    }

    @PostMapping
    @ApiOperation(SAVE_ADMIN_MODE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AdminModeFeatureIntegrationRequestDTO requestDTO) {
        adminModeIntegrationService.save(requestDTO);
        return created(create(API_V1 + BASE_INTEGRATION)).build();
    }

}
