package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.AppointmentServiceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentServiceTypeConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentServiceTypeConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentServiceTypeConstant.FETCH_NAME_AND_CODE_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentServiceTypeConstants.BASE_APPOINTMENT_SERVICE_TYPE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 26/05/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT_SERVICE_TYPE)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentServiceTypeResource {

    private final AppointmentServiceTypeService appointmentServiceTypeService;

    public AppointmentServiceTypeResource(AppointmentServiceTypeService appointmentServiceTypeService) {
        this.appointmentServiceTypeService = appointmentServiceTypeService;
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveMinInfo() {
        return ok(appointmentServiceTypeService.fetchActiveMinInfo());
    }

    @GetMapping(NAME + CODE)
    @ApiOperation(FETCH_NAME_AND_CODE_FOR_DROPDOWN)
    public ResponseEntity<?> fetchSerivceTypeNameAndCodeList() {
        return ok(appointmentServiceTypeService.fetchServiceTypeNameAndCodeList());
    }

}
