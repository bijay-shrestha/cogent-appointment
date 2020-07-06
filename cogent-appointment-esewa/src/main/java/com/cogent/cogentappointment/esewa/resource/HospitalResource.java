package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentappointment.esewa.constants.StringConstant.DATA;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.HospitalConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentServiceType.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.utils.JWTDecryptUtils.decrypt;
import static com.cogent.cogentappointment.esewa.utils.JWTDecryptUtils.toDecrypt;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;
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
    public ResponseEntity<?> fetchMinDetails(@RequestBody Map<String, String> data) throws IOException {

        HospitalMinSearchRequestDTO searchRequestDTO = convertValue(toDecrypt(data),
                HospitalMinSearchRequestDTO.class);

        return ok(hospitalService.fetchMinDetails(searchRequestDTO));
    }

    @GetMapping(APPOINTMENT_SERVICE_TYPE + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE)
    public ResponseEntity<?> fetchHospitalAppointmentServiceType(@PathVariable("hospitalId") Long hospitalId) {
        return ok(hospitalService.fetchHospitalAppointmentServiceType(hospitalId));
    }

}
