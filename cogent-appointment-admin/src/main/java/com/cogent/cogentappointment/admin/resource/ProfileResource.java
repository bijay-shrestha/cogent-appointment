package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.ProfileConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DepartmentConstants.DEPARTMENT_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ProfileSetupConstants.BASE_PROFILE;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 7/2/19
 */
@RestController
@RequestMapping(value = API_V1 + BASE_PROFILE)
@Api(BASE_API_VALUE)
public class ProfileResource {

    private final ProfileService profileService;

    public ProfileResource(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ProfileRequestDTO requestDTO) {
        profileService.save(requestDTO);
        return created(create(API_V1 + BASE_PROFILE)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<Void> update(@Valid @RequestBody ProfileUpdateRequestDTO requestDTO) {
        profileService.update(requestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> deleteProfile(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        profileService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody ProfileSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(profileService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(profileService.fetchDetailsById(id));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchProfilesForDropdown() {
        return ok(profileService.fetchActiveProfilesForDropdown());
    }

    @GetMapping(ACTIVE + MIN + DEPARTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_PROFILE_BY_DEPARTMENT_ID)
    public ResponseEntity<?> fetchActiveProfilesForDropdown(@PathVariable("departmentId") Long departmentId) {
        return ok(profileService.fetchProfileByDepartmentId(departmentId));
    }

    @GetMapping(MIN + DEPARTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_PROFILE_BY_DEPARTMENT_ID)
    public ResponseEntity<?> fetchProfilesForDropdown(@PathVariable("departmentId") Long departmentId) {
        return ok(profileService.fetchAllProfileByDepartmentId(departmentId));
    }
}