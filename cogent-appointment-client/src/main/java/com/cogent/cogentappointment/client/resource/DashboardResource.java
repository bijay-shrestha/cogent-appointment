package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.dashboard.AppointmentCountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DashboardConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DashboardConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalConstants.COUNT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalConstants.REGISTERED;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa २०/२/१०
 */
@RequestMapping(API_V1 + BASE_DASHBOARD)
@RestController
@Api(BASE_API_VALUE)
public class DashboardResource {

    private DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PutMapping(GENERATE_REVENUE)
    @ApiOperation(GENERATE_REVENUE_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody GenerateRevenueRequestDTO requestDTO) {
        return ok(dashboardService.getRevenueGeneratedDetail(requestDTO));
    }

    @PutMapping(OVER_ALL_APPOINTMENT)
    @ApiOperation(OVER_ALL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> getOverAllAppointment(@Valid @RequestBody AppointmentCountRequestDTO countRequestDTO) {
        return ok(dashboardService.countOverAllAppointment(countRequestDTO));
    }

    @GetMapping(REGISTERED + COUNT)
    @ApiOperation(COUNT_REGISTERED_PATIENTS)
    public ResponseEntity<?> countRegisteredPatients() {
        return ok(dashboardService.countRegisteredPatients());
    }
}
