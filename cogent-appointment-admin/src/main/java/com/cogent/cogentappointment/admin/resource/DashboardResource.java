package com.cogent.cogentappointment.admin.resource;


import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.FETCH_APPOINTMENT_QUEUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DashboardConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DashboardConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@RequestMapping(API_V1 + BASE_DASHBOARD)
@RestController
@Api(BASE_API_VALUE)
public class DashboardResource {

    private DashboardService dashboardService;
    private AppointmentService appointmentService;

    public DashboardResource(DashboardService dashboardService, AppointmentService appointmentService) {
        this.dashboardService = dashboardService;
        this.appointmentService = appointmentService;
    }

    @PutMapping(GENERATE_REVENUE)
    @ApiOperation(GENERATE_REVENUE_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody GenerateRevenueRequestDTO requestDTO) {
        return ok(dashboardService.getRevenueGeneratedDetail(requestDTO));
    }

    @PutMapping(OVER_ALL_APPOINTMENT)
    @ApiOperation(OVER_ALL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> getOverAllAppointment(@Valid @RequestBody DashBoardRequestDTO countRequestDTO) throws NoSuchAlgorithmException {
        return ok(dashboardService.countOverallAppointments(countRequestDTO));
    }

    @GetMapping(REGISTERED + COUNT + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(COUNT_REGISTERED_PATIENTS_OPERATION)
    public ResponseEntity<?> countRegisteredPatients(@PathVariable("hospitalId") Long hospitalId) {
        return ok(dashboardService.countOverallRegisteredPatients(hospitalId));
    }

    @PutMapping(REVENUE_STATISTICS)
    @ApiOperation(REVENUE_STATISTICS_OPERATION)
    public ResponseEntity<?> getRevenueStatistics(@Valid @RequestBody DashBoardRequestDTO countRequestDTO) {
        return ok(dashboardService.getRevenueStatistic(countRequestDTO));
    }

    @PutMapping(APPOINTMENT_QUEUE)
    @ApiOperation(FETCH_APPOINTMENT_QUEUE)
    public ResponseEntity<?> fetchTodayAppointmentQueue(@RequestBody AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                        @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchTodayAppointmentQueue(appointmentQueueRequestDTO, pageable));
    }

    @PutMapping(APPOINTMENT_QUEUE_BY_TIME)
    @ApiOperation(FETCH_APPOINTMENT_QUEUE)
    public ResponseEntity<?> fetchTodayAppointmentQueueByTime(@RequestBody AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchTodayAppointmentQueueByTime(appointmentQueueRequestDTO, pageable));
    }

}
