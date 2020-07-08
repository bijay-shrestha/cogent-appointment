package com.cogent.cogentappointment.esewa.resource.v1;

import com.cogent.cogentappointment.esewa.service.HospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.HospitalDepartmentConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.MIN;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 28/05/20
 */
@RestController
@RequestMapping(API_V1 + BASE_HOSPITAL_DEPARTMENT)
@Api(BASE_HOSPITAL_DEPARTMENT_API_VALUE)
public class HospitalDepartmentResource {

    private final HospitalDepartmentService hospitalDepartmentService;

    public HospitalDepartmentResource(HospitalDepartmentService hospitalDepartmentService) {
        this.hospitalDepartmentService = hospitalDepartmentService;
    }

    @GetMapping(HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ACTIVE_MIN_HOSPITAL_DEPARTMENT_INFO)
    public ResponseEntity<?> fetchActiveMinDepartment(@PathVariable("hospitalId") Long hospitalId) {
        return ok(hospitalDepartmentService.fetchActiveMinHospitalDepartment(hospitalId));
    }

    @GetMapping(BILLING_MODE + HOSPITAL_DEPARTMENT_ID)
    @ApiOperation(FETCH_ACTIVE_MIN_HOSPITAL_DEPARTMENT_INFO)
    public ResponseEntity<?> fetchBillingModeByDepartmentId(@PathVariable("hospitalDepartmentId") Long hospitalDepartmentId) {
        return ok(hospitalDepartmentService.fetchBillingModeByDepartmentId(hospitalDepartmentId));
    }

    @GetMapping(MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_COMBINED_HOSPITAL_DEPARTMENT_INFO)
    public ResponseEntity<?> fetchHospitalDepartmentInfo(@PathVariable("hospitalId") Long hospitalId) {
        return ok(hospitalDepartmentService.fetchHospitalDepartmentInfo(hospitalId));
    }

}
