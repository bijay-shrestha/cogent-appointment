package com.cogent.cogentappointment.esewa.resource.v2;

import com.cogent.cogentappointment.esewa.dto.request.DataWrapperRequest;
import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.HospitalConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentServiceType.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 12/01/2020
 */
@RestController(API_V2 + BASE_HOSPITAL)
@RequestMapping(API_V2 + BASE_HOSPITAL)
@Api(BASE_API_VALUE)
public class HospitalResource {

    private final HospitalService hospitalService;

    private final DataWrapperRequest dataWrapperRequest;

    public HospitalResource(HospitalService hospitalService,
                            DataWrapperRequest dataWrapperRequest) {
        this.hospitalService = hospitalService;
        this.dataWrapperRequest = dataWrapperRequest;
    }

    @PutMapping(SEARCH + MIN)
    @ApiOperation(FETCH_MIN_DETAILS)
    public ResponseEntity<?> fetchMinDetails() throws IOException {

        HospitalMinSearchRequestDTO searchRequestDTO = convertValue(dataWrapperRequest.getData(),
                HospitalMinSearchRequestDTO.class);

        return ok(hospitalService.fetchMinDetails(searchRequestDTO));
    }

    @GetMapping(APPOINTMENT_SERVICE_TYPE + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE)
    public ResponseEntity<?> fetchHospitalAppointmentServiceType(@PathVariable("hospitalId") Long hospitalId) {
        return ok(hospitalService.fetchHospitalAppointmentServiceType(hospitalId));
    }

}
