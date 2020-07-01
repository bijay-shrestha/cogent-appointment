package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.client.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.HospitalConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentServiceTypeConstants.BASE_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL;
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

    /*esewa*/
    @PutMapping(SEARCH + MIN)
    @ApiOperation(FETCH_MIN_DETAILS)
    public ResponseEntity<?> fetchMinDetails(@RequestBody HospitalMinSearchRequestDTO searchRequestDTO) {
        return ok(hospitalService.fetchMinDetails(searchRequestDTO));
    }

    @GetMapping(BASE_APPOINTMENT_SERVICE_TYPE)
    @ApiOperation(FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE)
    public ResponseEntity<?> fetchAssignedHospitalAppointmentServiceType() {
        return ok(hospitalService.fetchAssignedAppointmentServiceType());

    }
}
