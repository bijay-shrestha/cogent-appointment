package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.AppointmentStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentStatusConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentStatusConstant.FETCH_APPOINTMENT_STATUS;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.STATUS;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 16/12/2019
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentStatusResource {

    private final AppointmentStatusService appointmentStatusService;

    public AppointmentStatusResource(AppointmentStatusService appointmentStatusService) {
        this.appointmentStatusService = appointmentStatusService;
    }

    @PutMapping(STATUS)
    @ApiOperation(FETCH_APPOINTMENT_STATUS)
    @ApiImplicitParams({@ApiImplicitParam(name = "toDate", value = "dd/MM/yyyy", required = true,
            dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "fromDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query")})
    public ResponseEntity<?> fetchAppointmentStatus(@DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate,
                                                    @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate,
                                                    @RequestParam("specializationId") Long specializationId,
                                                    @RequestParam("doctorId") Long doctorId,
                                                    @RequestParam("status") String status) {
        return ok(appointmentStatusService.fetchAppointmentStatusResponseDTO(toDate, fromDate, specializationId,
                doctorId, status));
    }
}
