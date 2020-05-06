package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.BreakTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.BreakTypeConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.BreakTypeConstant.FETCH_BREAK_TYPE_BY_HOSPITAL_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.BreakTypeConstants.BASE_BREAK_TYPE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 06/05/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_BREAK_TYPE)
@Api(BASE_API_VALUE)
public class BreakTypeResource {

    private final BreakTypeService breakTypeService;

    public BreakTypeResource(BreakTypeService breakTypeService) {
        this.breakTypeService = breakTypeService;
    }

    @GetMapping(ACTIVE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BREAK_TYPE_BY_HOSPITAL_OPERATION)
    public ResponseEntity<?> fetchBreakTypeByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(breakTypeService.fetchBreakTypeByHospitalId(hospitalId));
    }

}
