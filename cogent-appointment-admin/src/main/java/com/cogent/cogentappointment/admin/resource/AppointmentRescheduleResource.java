package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleDTO;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.FETCH_PENDING_APPOINTMENT_APPROVAL;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Rupak
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentRescheduleResource {

    private AppointmentService appointmentService;

    public AppointmentRescheduleResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @PutMapping(RESCHEDULE)
    @ApiOperation(FETCH_PENDING_APPOINTMENT_APPROVAL)
    public ResponseEntity<?> search(@RequestBody AppointmentRescheduleDTO rescheduleDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.rescheduleAppointment(rescheduleDTO, pageable));
    }

}
