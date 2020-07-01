package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.dashboard.*;
import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.service.AppointmentService;
import com.cogent.cogentappointment.client.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DashboardConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DashboardConstants.*;
import static com.cogent.cogentappointment.client.utils.DoctorRevenueUtils.convertToDoctorRevenueRequestDTO;
import static com.cogent.cogentappointment.client.utils.HospitalDepartmentRevenueUtils.convertToHospitalDepartmentRevenueRequestDTO;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
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

    @PutMapping(GENERATE_REVENUE)
    @ApiOperation(GENERATE_REVENUE_OPERATION)
    public ResponseEntity<?> getRevenueStatistics(@Valid @RequestBody GenerateRevenueRequestDTO requestDTO) {
        return ok(dashboardService.getRevenueStatistics(requestDTO));
    }

    @PutMapping(OVER_ALL_APPOINTMENT)
    @ApiOperation(OVER_ALL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> getOverAllAppointment(@Valid @RequestBody DashBoardRequestDTO countRequestDTO) {
        return ok(dashboardService.countOverallAppointments(countRequestDTO));
    }

    @GetMapping(REGISTERED + COUNT)
    @ApiOperation(COUNT_REGISTERED_PATIENTS_OPERATION)
    public ResponseEntity<?> countRegisteredPatients() {
        return ok(dashboardService.getPatientStatistics());
    }

    @PutMapping(REVENUE_STATISTICS)
    @ApiOperation(REVENUE_STATISTICS_OPERATION)
    public ResponseEntity<?> getRevenueTrend(@Valid @RequestBody DashBoardRequestDTO countRequestDTO) {
        return ok(dashboardService.getRevenueTrend(countRequestDTO));
    }

    @PutMapping(APPOINTMENT_QUEUE)
    @ApiOperation(FETCH_APPOINTMENT_QUEUE)
    public ResponseEntity<?> fetchAppointmentQueueLog(@RequestBody AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchAppointmentQueueLog(appointmentQueueRequestDTO, pageable));
    }

    @PutMapping(APPOINTMENT_QUEUE_BY_TIME)
    @ApiOperation(FETCH_APPOINTMENT_QUEUE)
    public ResponseEntity<?> fetchTodayAppointmentQueueByTime(@RequestBody AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchTodayAppointmentQueueByTime(appointmentQueueRequestDTO, pageable));
    }

    @PutMapping(TOTAL_REFUNDED_AMOUNT)
    @ApiOperation(REFUND_AMOUNT_OPERATION)
    public ResponseEntity<?> calculateTotalRefundedAmount(@RequestBody RefundAmountRequestDTO refundAmountRequestDTO) {
        return ok().body(dashboardService.calculateTotalRefundedAmount(refundAmountRequestDTO));
    }

    @GetMapping(DOCTOR_REVENUE)
    @ApiOperation(DOCTOR_REVENUE_OPERATION)
    public ResponseEntity<?> getDoctorRevenueList(@RequestParam("toDate") String toDate,
                                                  @RequestParam("fromDate") String fromDate,
                                                  @RequestParam("doctorId") Long doctorId,
                                                  @RequestParam("specializationId") Long specializationId,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("size") int size) throws ParseException {

        DoctorRevenueRequestDTO doctorRevenueRequestDTO =
                convertToDoctorRevenueRequestDTO(doctorId, getLoggedInHospitalId(), specializationId, fromDate, toDate);
        Pageable pageable = PageRequest.of(page, size);
        return ok(dashboardService.calculateOverallDoctorRevenue(doctorRevenueRequestDTO, pageable));
    }

    @GetMapping(HOSPITAL_DEPARTMENT_REVENUE)
    @ApiOperation(HOSPITAL_DEPARTMENT_REVENUE_OPERATION)
    public ResponseEntity<?> getHospitalDepartmentRevenueList(@RequestParam("toDate") String toDate,
                                                              @RequestParam("fromDate") String fromDate,
                                                              @RequestParam("hospitalDepartmentId") Long hospitalDepartmentId,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) throws ParseException {

        HospitalDepartmentRevenueRequestDTO revenueRequestDTO =
                convertToHospitalDepartmentRevenueRequestDTO(hospitalDepartmentId, fromDate, toDate);

        Pageable pageable = PageRequest.of(page, size);
        return ok(dashboardService.calculateOverallHospitalDeptRevenue(revenueRequestDTO, pageable));
    }

    @PutMapping(DYNAMIC_DASHBOARD_FEATURE + ADMIN_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DYNAMIC_DASHBOARD_FEATURE)
    public ResponseEntity<?> fetchDashboardEntityByAdmin(@PathVariable("adminId") Long adminId) {
        return ok(dashboardService.getDashboardFeaturesByAdmin(adminId));
    }

    @GetMapping(DYNAMIC_DASHBOARD_FEATURE)
    @ApiOperation(OVER_ALL_DASHBOARD_FEATURE)
    public ResponseEntity<?> fetchAllDashboardFeature() {
        return ok(dashboardService.fetchAllDashboardFeature());
    }
}
