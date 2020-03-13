package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.service.EsewaService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.EsewaConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.EsewaConstants.BASE_ESEWA;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = API_V1 + BASE_ESEWA)
@Api(BASE_API_VALUE)
public class eSewaResource {

    private final EsewaService esewaService;

    public eSewaResource(EsewaService esewaService) {
        this.esewaService = esewaService;
    }

    @PutMapping("/vacantAppointmentDates")
    public ResponseEntity<?> fetchAppointmentLog(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.doctorAvailableTime(requestDTO));
    }

}
