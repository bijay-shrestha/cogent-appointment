package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.CommonConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.CommonConstant.FETCH_DOCTOR_SPECIALIZATION_INFO;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.CommonConstants.BASE_COMMON;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.CommonConstants.DOCTOR_SPECIALIZATION;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.INFO;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 05/02/2020
 */
@RequestMapping(API_V1 + BASE_COMMON)
@RestController
@Api(BASE_API_VALUE)
public class CommonResource {

    private final CommonService commonService;

    public CommonResource(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping(DOCTOR_SPECIALIZATION + INFO + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DOCTOR_SPECIALIZATION_INFO)
    public ResponseEntity<?> fetchDoctorSpecializationInfo(@PathVariable("hospitalId") Long hospitalId) {
        return ok(commonService.fetchDoctorSpecializationInfo(hospitalId));
    }

}
