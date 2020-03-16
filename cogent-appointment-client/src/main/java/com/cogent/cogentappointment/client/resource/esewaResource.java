package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.service.EsewaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.EsewaConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.EsewaConstants.*;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = API_V1 + BASE_ESEWA)
@Api(BASE_API_VALUE)
public class esewaResource {

    private final EsewaService esewaService;

    public esewaResource(EsewaService esewaService) {
        this.esewaService = esewaService;
    }

    @PutMapping("/vacantAppointmentDates")
    public ResponseEntity<?> doctorAvailableTime(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.doctorAvailableTime(requestDTO));
    }

    @PutMapping(FETCH_DOCTOR_AVAILABLE_STATUS)
    @ApiOperation(FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION)
    public ResponseEntity<?> fetchDoctorAvailableStatus(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(esewaService.fetchDoctorAvailableStatus(requestDTO));
    }

    @PutMapping(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION)
    @ApiOperation(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION)
    public ResponseEntity<?> fetchAvailableDoctorWithSpecialization(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDoctorWithSpecialization(requestDTO));
    }


}
