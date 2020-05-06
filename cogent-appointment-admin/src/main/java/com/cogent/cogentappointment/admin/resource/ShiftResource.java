package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.ShiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ShiftConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ShiftConstant.FETCH_SHIFT_BY_HOSPITAL_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ShiftConstants.BASE_SHIFT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 06/05/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_SHIFT)
@Api(BASE_API_VALUE)
public class ShiftResource {

    private final ShiftService shiftService;

    public ShiftResource(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping(ACTIVE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_SHIFT_BY_HOSPITAL_OPERATION)
    public ResponseEntity<?> fetchShiftByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(shiftService.fetchShiftByHospitalId(hospitalId));
    }

}
