package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminInfoByUsernameResponseDTO;
import com.cogent.cogentappointment.admin.service.AdminService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

import static java.net.URI.create;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.AdminConstants.BASE_ADMIN)
@Api(SwaggerConstants.AdminConstant.BASE_API_VALUE)
public class AdminResource {

    private final AdminService adminService;

    public AdminResource(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SwaggerConstants.AdminConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("request") String request,
                                  HttpServletRequest httpServletRequest) throws IOException {

        AdminRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, AdminRequestDTO.class);
        adminService.save(adminRequestDTO, file, httpServletRequest);
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.AdminConstants.BASE_ADMIN)).build();
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.AdminConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveAdminsForDropdown() {
        return ok(adminService.fetchActiveAdminsForDropdown());
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.AdminConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AdminSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(adminService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.AdminConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(adminService.fetchDetailsById(id));
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.AdminConstant.DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        adminService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.AdminConstants.CHANGE_PASSWORD)
    @ApiOperation(SwaggerConstants.AdminConstant.CHANGE_PASSWORD_OPERATION)
    public ResponseEntity<?> changePassword(@Valid @RequestBody AdminChangePasswordRequestDTO requestDTO) {
        adminService.changePassword(requestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.AdminConstants.RESET_PASSWORD)
    @ApiOperation(SwaggerConstants.AdminConstant.RESET_PASSWORD_OPERATION)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AdminResetPasswordRequestDTO requestDTO) {
        adminService.resetPassword(requestDTO);
        return ok().build();
    }

    @PutMapping(value = WebResourceKeyConstants.AdminConstants.AVATAR, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SwaggerConstants.AdminConstant.UPDATE_AVATAR_OPERATION)
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "files", required = false) MultipartFile file,
                                          @RequestParam("adminId") Long adminId) {
        adminService.updateAvatar(file, adminId);
        return ok().build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SwaggerConstants.AdminConstant.UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("request") String request) throws IOException {

        AdminUpdateRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, AdminUpdateRequestDTO.class);
        adminService.update(adminRequestDTO, file);
        return ok().build();
    }

    @GetMapping(WebResourceKeyConstants.AdminConstants.VERIFY)
    @ApiOperation(SwaggerConstants.AdminConstant.VERIFY_ADMIN)
    public ResponseEntity<?> verify(@RequestParam(name = "token") String token) {
        adminService.verifyConfirmationToken(token);
        return ok().build();
    }

    @PostMapping(WebResourceKeyConstants.BASE_PASSWORD)
    @ApiOperation(SwaggerConstants.AdminConstant.SAVE_PASSWORD_OPERATION)
    public ResponseEntity<?> savePassword(@Valid @RequestBody AdminPasswordRequestDTO requestDTO) {
        adminService.savePassword(requestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.AdminConstants.INFO)
    @ApiOperation(SwaggerConstants.AdminConstant.FETCH_LOGGED_IN_ADMIN_INFO)
    public ResponseEntity<?> fetchLoggedInAdminInfo(@Valid @RequestBody AdminInfoRequestDTO requestDTO) {
        return ok(adminService.fetchLoggedInAdminInfo(requestDTO));
    }

    @GetMapping(WebResourceKeyConstants.USERNAME_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.AdminConstant.FETCH_INFO_BY_USERNAME)
    public AdminInfoByUsernameResponseDTO fetchAdminInfoByUsername(@PathVariable("username") String username) {
        return adminService.fetchAdminInfoByUsername(username);
    }

    @GetMapping(WebResourceKeyConstants.AdminConstants.ASSIGNED_SUB_DEPARTMENTS + WebResourceKeyConstants.USERNAME_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.AdminConstant.FETCH_ASSIGNED_SUB_DEPARTMENTS)
    public ResponseEntity<?> fetchLoggedInAdminSubDepartmentInfo(@PathVariable("username") String username) {
        return ok(adminService.fetchLoggedInAdminSubDepartmentList(username));
    }

    @GetMapping(WebResourceKeyConstants.AdminConstants.ADMIN_META_INFO)
    @ApiOperation(SwaggerConstants.AdminConstant.FETCH_ADMIN_META_INFO)
    public ResponseEntity<?> fetchAdminMetaInfoDropdown() {
        return ok(adminService.fetchAdminMetaInfoResponseDto());
    }
}
