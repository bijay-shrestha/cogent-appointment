package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.client.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SideBarConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SideBarConstant.FETCH_ASSIGNED_PROFILE_RESPONSE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SidebarConstants.BASE_SIDE_BAR;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 27/12/2019
 */
@RestController
@RequestMapping(value = API_V1 + BASE_SIDE_BAR)
@Api(BASE_API_VALUE)
public class SideBarResource {

    private final ProfileService profileService;

    public SideBarResource(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PutMapping
    @ApiOperation(FETCH_ASSIGNED_PROFILE_RESPONSE)
    public ResponseEntity<?> fetchAssignedProfileResponse(@Valid @RequestBody ProfileMenuSearchRequestDTO requestDTO) {
        return ok(profileService.fetchAssignedProfile(requestDTO));
    }
}
