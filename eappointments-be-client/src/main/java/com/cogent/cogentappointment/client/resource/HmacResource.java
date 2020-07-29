package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.HmacService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.HmaConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.APPOINTMENT_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HMAC;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 6/19/20
 */

@RestController
@RequestMapping(value = API_V1 + HMAC)
@Api(BASE_API_VALUE)
public class HmacResource {

    private final HmacService hmacService;

    public HmacResource(HmacService hmacService) {
        this.hmacService = hmacService;
    }

    @GetMapping(APPOINTMENT_ID_PATH_VARIABLE_BASE)
    public ResponseEntity<?> generateHash(@PathVariable("appointmentId") Long appointmentId) {
        return ok(hmacService.getHmacForFrontend(appointmentId));
    }
}
