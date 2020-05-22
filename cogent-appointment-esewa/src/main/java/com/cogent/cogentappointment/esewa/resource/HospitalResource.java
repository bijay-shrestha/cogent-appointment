package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.HospitalConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.HospitalConstant.FETCH_MIN_DETAILS;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.MIN;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.SEARCH;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 12/01/2020
 */
@RestController
@RequestMapping(API_V1 + BASE_HOSPITAL)
@Api(BASE_API_VALUE)
public class HospitalResource {

    private final HospitalService hospitalService;

    public HospitalResource(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PutMapping(SEARCH + MIN)
    @ApiOperation(FETCH_MIN_DETAILS)
    public ResponseEntity<?> fetchMinDetails(@RequestBody HospitalMinSearchRequestDTO searchRequestDTO) {
        return ok(hospitalService.fetchMinDetails(searchRequestDTO));
    }
}
