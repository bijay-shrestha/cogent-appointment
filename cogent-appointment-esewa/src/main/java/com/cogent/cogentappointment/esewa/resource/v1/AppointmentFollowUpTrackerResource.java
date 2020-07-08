package com.cogent.cogentappointment.esewa.resource.v1;

import com.cogent.cogentappointment.esewa.dto.request.appointment.followup.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentFollowUpTrackerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.FollowUpTrackerConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.FollowUpTrackerConstant.FETCH_FOLLOW_UP_DETAILS;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.FOLLOW_UP;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 16/02/20
 */
@RestController
@RequestMapping(API_V1 + BASE_APPOINTMENT + FOLLOW_UP)
@Api(BASE_API_VALUE)
public class AppointmentFollowUpTrackerResource {

    private final AppointmentFollowUpTrackerService followUpTrackerService;

    public AppointmentFollowUpTrackerResource(AppointmentFollowUpTrackerService followUpTrackerService) {
        this.followUpTrackerService = followUpTrackerService;
    }

    @PutMapping
    @ApiOperation(FETCH_FOLLOW_UP_DETAILS)
    public ResponseEntity<?> fetchFollowUpTrackerDetails(@Valid @RequestBody AppointmentFollowUpRequestDTO requestDTO) {
        return ok().body(followUpTrackerService.fetchAppointmentFollowUpDetails(requestDTO));
    }

}
