package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 7/2/19
 */
@RestController
@RequestMapping(value = WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.ProfileSetupConstants.BASE_PROFILE)
@Api(SwaggerConstants.ProfileConstant.BASE_API_VALUE)
public class ProfileResource {

    private final ProfileService profileService;

    public ProfileResource(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ApiOperation(SwaggerConstants.ProfileConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody ProfileRequestDTO requestDTO) {
        profileService.save(requestDTO);
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.ProfileSetupConstants.BASE_PROFILE)).build();
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.ProfileConstant.UPDATE_OPERATION)
    public ResponseEntity<Void> update(@Valid @RequestBody ProfileUpdateRequestDTO requestDTO) {
        profileService.update(requestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.ProfileConstant.DELETE_OPERATION)
    public ResponseEntity<?> deleteProfile(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        profileService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.ProfileConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody ProfileSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(profileService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.ProfileConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(profileService.fetchDetailsById(id));
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.ProfileConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchProfilesForDropdown() {
        return ok(profileService.fetchActiveProfilesForDropdown());
    }

    @GetMapping(WebResourceKeyConstants.DepartmentConstants.DEPARTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.ProfileConstant.FETCH_PROFILE_BY_DEPARTMENT_ID)
    public ResponseEntity<?> fetchProfilesForDropdown(@PathVariable("departmentId") Long departmentId) {
        return ok(profileService.fetchProfileByDepartmentId(departmentId));
    }
}