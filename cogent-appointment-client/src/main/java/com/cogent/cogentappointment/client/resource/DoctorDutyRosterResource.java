package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.*;
import com.cogent.cogentappointment.client.service.DoctorDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DoctorDutyRosterConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DoctorDutyRosterConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 26/11/2019
 */
@RestController
@RequestMapping(value = API_V1 + BASE_DOCTOR_DUTY_ROSTER)
@Api(BASE_API_VALUE)
public class DoctorDutyRosterResource {

    private final DoctorDutyRosterService doctorDutyRosterService;

    public DoctorDutyRosterResource(DoctorDutyRosterService doctorDutyRosterService) {
        this.doctorDutyRosterService = doctorDutyRosterService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody DoctorDutyRosterRequestDTO requestDTO) {
        doctorDutyRosterService.save(requestDTO);
        return created(create(API_V1 + BASE_DOCTOR_DUTY_ROSTER)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody DoctorDutyRosterUpdateRequestDTO updateRequestDTO) {
        doctorDutyRosterService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        doctorDutyRosterService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(doctorDutyRosterService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(doctorDutyRosterService.fetchDetailsById(id));
    }

    @PutMapping(DOCTOR_DUTY_ROSTER_OVERRIDE)
    @ApiOperation(UPDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION)
    public ResponseEntity<?> updateDoctorDutyRosterOverride(
            @Valid @RequestBody DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {
        doctorDutyRosterService.updateDoctorDutyRosterOverride(updateRequestDTO);
        return ok().build();
    }

    @PutMapping(EXISTING)
    @ApiOperation(FETCH_EXISTING_ROSTERS)
    public ResponseEntity<?> fetchExistingDutyRosters(@Valid @RequestBody DoctorExistingDutyRosterRequestDTO requestDTO) {
        return ok(doctorDutyRosterService.fetchExistingDutyRosters(requestDTO));
    }

    @GetMapping(EXISTING + DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchExistingRosterDetails(@PathVariable("id") Long id) {
        return ok(doctorDutyRosterService.fetchExistingRosterDetails(id));
    }


//
//    @PutMapping(DOCTOR_DUTY_ROSTER_STATUS)
//    @ApiOperation(FETCH_DOCTOR_DUTY_ROSTER_STATUS_OPERATION)
//    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(
//            @RequestBody DoctorDutyRosterStatusRequestDTO searchRequestDTO) {
//        return doctorDutyRosterService.fetchDoctorDutyRosterStatus(searchRequestDTO);
//    }
}


