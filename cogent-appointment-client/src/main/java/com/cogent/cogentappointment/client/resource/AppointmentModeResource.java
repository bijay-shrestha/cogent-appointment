package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.AppointmentModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentModeConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentModeConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentModeConstants.BASE_APPOINTMENT_MODE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 14/07/20
 */

@RestController
@RequestMapping(value = API_V1 + BASE_APPOINTMENT_MODE)
@Api(BASE_API_VALUE)
public class AppointmentModeResource {

    private final AppointmentModeService appointmentModeService;

    public AppointmentModeResource(AppointmentModeService appointmentModeService) {
        this.appointmentModeService = appointmentModeService;
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveAppointmentMode() {
        return ok(appointmentModeService.fetchActiveMinAppointmentMode());
    }
}
