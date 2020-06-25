package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AdminConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AdminConstants.*;
import static java.net.URI.create;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = API_V1 + BASE_ADMIN)
@Api(BASE_API_VALUE)
public class AdminResource {

    private final AdminService adminService;

    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AdminRequestDTO requestDTO) {
        adminService.save(requestDTO);
        return created(create(API_V1 + BASE_ADMIN)).build();
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveAdminsForDropdown() {
        return ok(adminService.fetchActiveAdminsForDropdown());
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AdminSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(adminService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(adminService.fetchDetailsById(id));
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        adminService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(RESET_PASSWORD)
    @ApiOperation(RESET_PASSWORD_OPERATION)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AdminResetPasswordRequestDTO requestDTO) {
        adminService.resetPassword(requestDTO);
        return ok().build();
    }

    @PutMapping
    @ApiOperation(UPDATE_AVATAR_OPERATION)
    public ResponseEntity<?> updateAvatar(@Valid @RequestBody AdminAvatarUpdateRequestDTO updateRequestDTO) {
        adminService.updateAvatar(updateRequestDTO);
        return ok().build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody AdminUpdateRequestDTO adminUpdateRequestDTO) {
        adminService.update(adminUpdateRequestDTO);
        return ok().build();
    }

    @GetMapping(ADMIN_META_INFO)
    @ApiOperation(FETCH_ADMIN_META_INFO)
    public ResponseEntity<?> fetchAdminMetaInfoDropdown() {
        return ok(adminService.fetchAdminMetaInfoResponseDto());
    }

    @GetMapping(ADMIN_META_INFO_BY_COMPANY_ID + ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ADMIN_META_INFO_BY_COMPANY_ID)
    public ResponseEntity<?> fetchAdminMetaInfoDropdownByCompanyId(@PathVariable("id") Long id) {

        return ok(adminService.fetchAdminMetaInfoByCompanyIdResponseDto(id));
    }

    @GetMapping(ADMIN_META_INFO_BY_CLIENT_ID + ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ADMIN_META_INFO_BY_COMPANY_ID)
    public ResponseEntity<?> fetchAdminMetaInfoDropdownByClientId(@PathVariable("id") Long id) {

        return ok(adminService.fetchAdminMetaInfoByClientIdResponseDto(id));
    }
}
