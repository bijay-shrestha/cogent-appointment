package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.UniversityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AdminConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.UniversityConstant.FETCH_ACTIVE_UNIVERSITY;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.UniversityConstants.BASE_UNIVERSITY;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 30/01/2020
 */
@RestController
@RequestMapping(value = API_V1 + BASE_UNIVERSITY)
@Api(BASE_API_VALUE)
public class UniversityResource {

    private final UniversityService universityService;

    public UniversityResource(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    @ApiOperation(FETCH_ACTIVE_UNIVERSITY)
    public ResponseEntity<?> fetchActiveUniversity() {
        return ok(universityService.fetchActiveUniversity());
    }
}
