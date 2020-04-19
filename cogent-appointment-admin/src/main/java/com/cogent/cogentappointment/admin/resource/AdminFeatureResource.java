package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.adminFeature.AdminFeatureRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminFeatureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AdminFeatureConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AdminFeatureConstant.UPDATE_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AdminFeatureConstants.BASE_ADMIN_FEATURE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 18/04/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_ADMIN_FEATURE)
@Api(BASE_API_VALUE)
public class AdminFeatureResource {

    private final AdminFeatureService adminFeatureService;

    public AdminFeatureResource(AdminFeatureService adminFeatureService) {
        this.adminFeatureService = adminFeatureService;
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid AdminFeatureRequestDTO requestDTO) {
        adminFeatureService.update(requestDTO);
        return ok().build();
    }
}
