package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override.DDROverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.DDRShiftWiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DoctorDutyRosterShiftWiseConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DDRShiftWiseConstants.*;
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
    public ResponseEntity<?> saveDDROverrideDetail(@RequestBody DDROverrideRequestDTO requestDTO) {
        ddrShiftWiseService.saveDDROverrideDetail(requestDTO);
        return created(create(API_V1 + BASE_DDR_SHIFT_WISE + OVERRIDE)).build();
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

    @PutMapping(EXISTING + WEEK_DAYS)
    @ApiOperation(FETCH_EXISTING_WEEK_DAYS_DETAIL)
    public ResponseEntity<?> fetchExistingDDRWeekDaysDetail(@Valid @RequestBody DDRWeekDaysRequestDTO requestDTO) {
        return ok(ddrShiftWiseService.fetchExistingDDRWeekDaysDetail(requestDTO));
    }

    @GetMapping(EXISTING + WEEK_DAYS + BREAK_DETAIL + DDR_WEEK_DAYS_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_EXISTING_WEEK_DAYS_BREAK_DETAIL)
    public ResponseEntity<?> fetchDDRWeekDaysBreakDetail(@PathVariable("ddrWeekDaysId") Long ddrWeekDaysId) {
        return ok(ddrShiftWiseService.fetchWeekDaysBreakDetail(ddrWeekDaysId));
    }

    @GetMapping(EXISTING + OVERRIDE + BREAK_DETAIL + DDR_OVERRIDE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_EXISTING_OVERRIDE_BREAK_DETAIL)
    public ResponseEntity<?> fetchExistingOverrideBreakDetail(@PathVariable("ddrOverrideId") Long ddrOverrideId) {
        return ok(ddrShiftWiseService.fetchExistingOverrideBreakDetail(ddrOverrideId));
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody DDRSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(ddrShiftWiseService.search(searchRequestDTO, pageable));
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        ddrShiftWiseService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(DETAIL + DDR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("ddrId") Long ddrId) {
        return ok(ddrShiftWiseService.fetchDetailsById(ddrId));
    }

    @PutMapping(DETAIL + WEEK_DAYS)
    @ApiOperation(FETCH_WEEK_DAYS_DETAIL)
    public ResponseEntity<?> fetchDDRWeekDaysDetail(@Valid @RequestBody DDRWeekDaysRequestDTO requestDTO) {
        return ok(ddrShiftWiseService.fetchDDRWeekDaysDetail(requestDTO));
    }

    @GetMapping(OVERRIDE + BREAK_DETAIL + DDR_OVERRIDE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_OVERRIDE_BREAK_DETAIL)
    public ResponseEntity<?> fetchDDROverrideBreakDetail(@PathVariable("ddrOverrideId") Long ddrOverrideId) {
        return ok(ddrShiftWiseService.fetchDDROverrideBreakDetail(ddrOverrideId));
    }

    @PutMapping(UPDATE + OVERRIDE)
    @ApiOperation(UPDATE_DDR_OVERRIDE_OPERATION)
    public ResponseEntity<?> updateDDROverrideDetail (@Valid @RequestBody DDROverrideUpdateRequestDTO requestDTO){
        return ok(ddrShiftWiseService.updateDoctorDutyRosterOverride(requestDTO));
    }


}
