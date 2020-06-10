package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.HospitalDepartmentDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.HospitalDeptDutyRosterConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalDeptDutyRosterConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 20/05/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_HOSPITAL_DEPARTMENT_DUTY_ROSTER)
@Api(BASE_API_VALUE)
public class HospitalDepartmentDutyRosterResource {

    private final HospitalDepartmentDutyRosterService hospitalDepartmentDutyRosterService;

    public HospitalDepartmentDutyRosterResource(HospitalDepartmentDutyRosterService hospitalDepartmentDutyRosterService) {
        this.hospitalDepartmentDutyRosterService = hospitalDepartmentDutyRosterService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody HospitalDepartmentDutyRosterRequestDTO requestDTO) {
        hospitalDepartmentDutyRosterService.save(requestDTO);
        return created(create(API_V1 + BASE_HOSPITAL_DEPARTMENT_DUTY_ROSTER)).build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalDepartmentDutyRosterService.search(searchRequestDTO, pageable));
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        hospitalDepartmentDutyRosterService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(hospitalDepartmentDutyRosterService.fetchDetailsById(id));
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody HospitalDeptDutyRosterUpdateRequestDTO updateRequestDTO) {
        hospitalDepartmentDutyRosterService.update(updateRequestDTO);
        return ok().build();
    }

    @PutMapping(OVERRIDE)
    @ApiOperation(UPDATE_OVERRIDE_OPERATION)
    public ResponseEntity<?> updateOverride(@Valid @RequestBody
                                                    HospitalDeptDutyRosterOverrideUpdateRequestDTO updateRequestDTO) {
        return ok(hospitalDepartmentDutyRosterService.updateOverride(updateRequestDTO));
    }

    @DeleteMapping(OVERRIDE)
    @ApiOperation(DELETE_OVERRIDE_OPERATION)
    public ResponseEntity<?> deleteOverride(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        hospitalDepartmentDutyRosterService.deleteOverride(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(OVERRIDE + REVERT)
    @ApiOperation(REVERT_OVERRIDE_OPERATION)
    public ResponseEntity<?> revertDoctorDutyRosterOverride(
            @Valid @RequestBody List<HospitalDeptDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS) {
        hospitalDepartmentDutyRosterService.revertOverride(updateRequestDTOS);
        return ok().build();
    }

    @PutMapping(EXISTING)
    @ApiOperation(FETCH_EXISTING_ROSTERS)
    public ResponseEntity<?> fetchExistingDutyRosters(@Valid @RequestBody
                                                              HospitalDeptExistingDutyRosterRequestDTO requestDTO) {
        return ok(hospitalDepartmentDutyRosterService.fetchExistingDutyRosters(requestDTO));
    }

    @GetMapping(EXISTING + DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchExistingRosterDetails(@PathVariable("id") Long id) {
        return ok(hospitalDepartmentDutyRosterService.fetchExistingRosterDetails(id));
    }

}

