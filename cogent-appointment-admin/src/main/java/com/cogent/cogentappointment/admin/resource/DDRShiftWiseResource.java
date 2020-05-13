package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.service.DDRShiftWiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DoctorDutyRosterShiftWiseConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DDRShiftWiseConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DETAIL;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 08/05/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_DDR_SHIFT_WISE)
@Api(BASE_API_VALUE)
public class DDRShiftWiseResource {

    private final DDRShiftWiseService ddrShiftWiseService;

    public DDRShiftWiseResource(DDRShiftWiseService ddrShiftWiseService) {
        this.ddrShiftWiseService = ddrShiftWiseService;
    }

    @PostMapping
    @ApiOperation(SAVE_WEEK_DAYS_ROSTER_OPERATION)
    public ResponseEntity<?> saveDDRWeekDaysDetail(@Valid @RequestBody DDRRequestDTO requestDTO) {
        ddrShiftWiseService.saveDDRWeekDaysDetail(requestDTO);
        return created(create(API_V1 + BASE_DDR_SHIFT_WISE)).build();
    }

    @PostMapping(OVERRIDE)
    @ApiOperation(SAVE_OVERRIDE_ROSTER_OPERATION)
    public ResponseEntity<?> saveDDROverrideDetail (@RequestBody DDROverrideRequestDTO requestDTO){
        ddrShiftWiseService.saveDDROverrideDetail(requestDTO);
        return created(create(API_V1 + BASE_DDR_SHIFT_WISE+OVERRIDE)).build();
    }

    @PutMapping(EXISTING)
    @ApiOperation(FETCH_EXISTING_ROSTERS)
    public ResponseEntity<?> fetchMinExistingDDR(@Valid @RequestBody DDRExistingAvailabilityRequestDTO requestDTO) {
        return ok(ddrShiftWiseService.fetchMinExistingDDR(requestDTO));
    }

    @GetMapping(EXISTING + DETAIL + DDR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_EXISTING_ROSTERS_DETAIL)
    public ResponseEntity<?> fetchDetailExistingDDR(@PathVariable("ddrId") Long ddrId) {
        return ok(ddrShiftWiseService.fetchDetailExistingDDR(ddrId));
    }

    @GetMapping(EXISTING + WEEK_DAYS)
    @ApiOperation(FETCH_EXISTING_WEEK_DAYS_DETAIL)
    public ResponseEntity<?> fetchDDRWeekDaysDetail(@Valid @RequestBody DDRExistingWeekDaysRequestDTO requestDTO) {
        return ok(ddrShiftWiseService.fetchDDRWeekDaysDetail(requestDTO));
    }

}
