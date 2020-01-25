package com.cogent.cogentappointment.resource;

import com.cogent.admin.dto.commons.DeleteRequestDTO;
import com.cogent.admin.dto.request.doctorDutyRoster.*;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.admin.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.admin.service.DoctorDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.cogent.admin.constants.SwaggerConstants.DoctorDutyRosterConstant.*;
import static com.cogent.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.admin.constants.WebResourceKeyConstants.DoctorDutyRosterConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

;

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

    @GetMapping(DETAILS + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(doctorDutyRosterService.fetchDetailsById(id));
    }

    @PutMapping(CHECK_AVAILABILITY)
    @ApiOperation(CHECK_AVAILABILITY_OPERATION)
    public DoctorDutyRosterTimeResponseDTO fetchDetails(@Valid @RequestBody DoctorDutyRosterTimeRequestDTO requestDTO) {
        return doctorDutyRosterService.fetchDoctorDutyRosterTime(requestDTO);
    }

    @PutMapping(DOCTOR_DUTY_ROSTER_STATUS)
    @ApiOperation(FETCH_DOCTOR_DUTY_ROSTER_STATUS_OPERATION)
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(
            @RequestBody DoctorDutyRosterStatusRequestDTO searchRequestDTO) {
        return doctorDutyRosterService.fetchDoctorDutyRosterStatus(searchRequestDTO);
    }

    @PutMapping(DOCTOR_DUTY_ROSTER_OVERRIDE)
    @ApiOperation(UPDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_OPERATION)
    public ResponseEntity<?> updateDoctorDutyRosterOverride(
            @Valid @RequestBody DoctorDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {
        doctorDutyRosterService.updateDoctorDutyRosterOverride(updateRequestDTO);
        return ok().build();
    }
}


