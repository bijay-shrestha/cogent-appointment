package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.service.AdminLogService;
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
    private final AdminLogService adminLogService;

    public AdminResource(AdminService adminService, AdminLogService adminLogService) {
        this.adminService = adminService;
        this.adminLogService = adminLogService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("request") String request,
                                  @RequestBody AdminLogRequestDTO adminLogRequestDTO,
                                  HttpServletRequest httpServletRequest) throws IOException {

        AdminRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, AdminRequestDTO.class);
        adminService.save(adminRequestDTO, file, httpServletRequest);

        return created(create(API_V1 + BASE_ADMIN)).build();
    }

    @PutMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveAdminsForDropdown() {
        return ok(adminService.fetchActiveAdminsForDropdown());
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AdminSearchRequestDTO searchRequestDTO,
                                    @RequestBody AdminLogRequestDTO adminLogRequestDTO,
                                    HttpServletRequest httpServletRequest,
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

    @PutMapping(CHANGE_PASSWORD)
    @ApiOperation(CHANGE_PASSWORD_OPERATION)
    public ResponseEntity<?> changePassword(@Valid @RequestBody AdminChangePasswordRequestDTO requestDTO) {
        adminService.changePassword(requestDTO);
        return ok().build();
    }

    @PutMapping(RESET_PASSWORD)
    @ApiOperation(RESET_PASSWORD_OPERATION)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AdminResetPasswordRequestDTO requestDTO) {
        adminService.resetPassword(requestDTO);
        return ok().build();
    }

    @PutMapping(value = AVATAR, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_AVATAR_OPERATION)
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "files", required = false) MultipartFile file,
                                          @RequestParam("adminId") Long adminId) {
        adminService.updateAvatar(file, adminId);
        return ok().build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("request") String request) throws IOException {

        AdminUpdateRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, AdminUpdateRequestDTO.class);
        adminService.update(adminRequestDTO, file);
        return ok().build();
    }

    @GetMapping(VERIFY)
    @ApiOperation(VERIFY_ADMIN)
    public ResponseEntity<?> verify(@RequestParam(name = "token") String token) {
        adminService.verifyConfirmationToken(token);
        return ok().build();
    }

    @PostMapping(BASE_PASSWORD)
    @ApiOperation(SAVE_PASSWORD_OPERATION)
    public ResponseEntity<?> savePassword(@Valid @RequestBody AdminPasswordRequestDTO requestDTO) {
        adminService.savePassword(requestDTO);
        return ok().build();
    }

    @GetMapping(ADMIN_META_INFO)
    @ApiOperation(FETCH_ADMIN_META_INFO)
    public ResponseEntity<?> fetchAdminMetaInfoDropdown() {

        return ok(adminService.fetchAdminMetaInfoResponseDto());
    }

    @PutMapping(INFO)
    @ApiOperation(FETCH_LOGGED_IN_ADMIN_INFO)
    public ResponseEntity<?> fetchLoggedInAdminInfo(@Valid @RequestBody AdminInfoRequestDTO requestDTO) {

        return ok(adminService.fetchLoggedInAdminInfo(requestDTO));
    }


}
