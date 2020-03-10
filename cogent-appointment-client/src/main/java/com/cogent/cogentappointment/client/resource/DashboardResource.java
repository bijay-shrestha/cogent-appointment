package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.AppointmentService;
import com.cogent.cogentappointment.client.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DashboardConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DashboardConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@RequestMapping(API_V1 + BASE_DASHBOARD)
@RestController
@Api(BASE_API_VALUE)
public class DashboardResource {

    private final DashboardService dashboardService;

    private final AppointmentService appointmentService;

    public DashboardResource(DashboardService dashboardService,
                             AppointmentService appointmentService) {
        this.dashboardService = dashboardService;
        this.appointmentService = appointmentService;
    }

    @PutMapping(REVENUE_STATISTICS)
    @ApiOperation(REVENUE_STATISTICS_OPERATION)
    @ApiImplicitParams({@ApiImplicitParam(name = "currentToDate", value = "dd/MM/yyyy", required = true,
            dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "currentFromDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query"),
            @ApiImplicitParam(name = "previousToDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query"),
            @ApiImplicitParam(name = "previousFromDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query")})
    public ResponseEntity<?> revenueStatistics(@DateTimeFormat(pattern = "dd/MM/yyyy") Date previousToDate,
                                               @DateTimeFormat(pattern = "dd/MM/yyyy") Date previousFromDate,
                                               @DateTimeFormat(pattern = "dd/MM/yyyy") Date currentToDate,
                                               @DateTimeFormat(pattern = "dd/MM/yyyy") Date currentFromDate,
                                               @RequestParam("filterType") String filterType) {
        return ok(dashboardService.getRevenueStatistics(previousToDate, previousFromDate,
                currentToDate, currentFromDate, filterType.charAt(0)));
    }

    @PutMapping(OVER_ALL_APPOINTMENT)
    @ApiOperation(OVER_ALL_APPOINTMENT_OPERATION)
    @ApiImplicitParams({@ApiImplicitParam(name = "toDate", value = "dd/MM/yyyy", required = true, dataType = "date",
            paramType = "query"),
            @ApiImplicitParam(name = "fromDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query")})
    public ResponseEntity<?> overAllAppointment(@DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate,
                                                @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate) {
        return ok(dashboardService.countOverallAppointments(toDate, fromDate));
    }

    @GetMapping(REGISTERED + COUNT)
    @ApiOperation(PATIENTS_STATISTICS_OPERATION)
    public ResponseEntity<?> patientStatistics() {
        return ok(dashboardService.getPatientStatistics());
    }

    @PutMapping(REVENUE_TREND)
    @ApiOperation(REVENUE_TREND_OPERATION)
    @ApiImplicitParams({@ApiImplicitParam(name = "toDate", value = "dd/MM/yyyy", required = true, dataType = "date",
            paramType = "query"),
            @ApiImplicitParam(name = "fromDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query")})
    public ResponseEntity<?> revenueTrend(@DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate,
                                             @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate) {
        return ok(dashboardService.getRevenueTrend(toDate, fromDate));
    }

    @GetMapping(APPOINTMENT_QUEUE + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_APPOINTMENT_QUEUE)
    public ResponseEntity<?> todayAppointmentQueue(@PathVariable("doctorId") Long doctorId,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchTodayAppointmentQueue(doctorId, pageable));
    }

    @GetMapping(APPOINTMENT_QUEUE_BY_TIME + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_APPOINTMENT_QUEUE)
    public ResponseEntity<?> todayAppointmentQueueByTime(@PathVariable("doctorId") Long doctorId,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchTodayAppointmentQueueByTime(doctorId, pageable));
    }

    @PutMapping(DOCTOR_REVENUE_TRACKER)
    @ApiOperation(DOCTOR_REVENUE_TRACKER_OPERATION)
    @ApiImplicitParams({@ApiImplicitParam(name = "toDate", value = "dd/MM/yyyy", required = true, dataType = "date",
            paramType = "query"),
            @ApiImplicitParam(name = "fromDate", value = "dd/MM/yyyy", required = true, dataType = "date",
                    paramType = "query")})
    public ResponseEntity<?> doctorRevenueTracker(@DateTimeFormat(pattern = "dd/MM/yyyy") Date toDate,
                                                     @DateTimeFormat(pattern = "dd/MM/yyyy") Date fromDate,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok(dashboardService.getDoctorRevenueTracker(toDate, fromDate, pageable));
    }
}
