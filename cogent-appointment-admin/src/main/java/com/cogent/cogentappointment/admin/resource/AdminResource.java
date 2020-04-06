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

        saveLogs(adminLogRequestDTO, httpServletRequest);

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
                                    @RequestBody AdminLogRequestDTO adminLogRequestDTO,
                                    HttpServletRequest httpServletRequest,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);

        saveLogs(adminLogRequestDTO, httpServletRequest);

        return ok().body(adminService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest, @PathVariable("id") Long id) {

        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok(adminService.fetchDetailsById(id));
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminService.delete(deleteRequestDTO);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @PutMapping(CHANGE_PASSWORD)
    @ApiOperation(CHANGE_PASSWORD_OPERATION)
    public ResponseEntity<?> changePassword(@Valid @RequestBody AdminChangePasswordRequestDTO requestDTO, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminService.changePassword(requestDTO);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @PutMapping(RESET_PASSWORD)
    @ApiOperation(RESET_PASSWORD_OPERATION)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AdminResetPasswordRequestDTO requestDTO, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminService.resetPassword(requestDTO);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @PutMapping(value = AVATAR, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_AVATAR_OPERATION)
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "files", required = false) MultipartFile file,
                                          @RequestParam("adminId") Long adminId, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminService.updateAvatar(file, adminId);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("request") String request, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) throws IOException {

        AdminUpdateRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, AdminUpdateRequestDTO.class);
        adminService.update(adminRequestDTO, file);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @GetMapping(VERIFY)
    @ApiOperation(VERIFY_ADMIN)
    public ResponseEntity<?> verify(@RequestParam(name = "token") String token, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminService.verifyConfirmationToken(token);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @PostMapping(BASE_PASSWORD)
    @ApiOperation(SAVE_PASSWORD_OPERATION)
    public ResponseEntity<?> savePassword(@Valid @RequestBody AdminPasswordRequestDTO requestDTO, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminService.savePassword(requestDTO);
        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok().build();
    }

    @GetMapping(ADMIN_META_INFO)
    @ApiOperation(FETCH_ADMIN_META_INFO)
    public ResponseEntity<?> fetchAdminMetaInfoDropdown(@RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {

        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok(adminService.fetchAdminMetaInfoResponseDto());
    }

    @PutMapping(INFO)
    @ApiOperation(FETCH_LOGGED_IN_ADMIN_INFO)
    public ResponseEntity<?> fetchLoggedInAdminInfo(@Valid @RequestBody AdminInfoRequestDTO requestDTO, @RequestBody AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {

        saveLogs(adminLogRequestDTO, httpServletRequest);
        return ok(adminService.fetchLoggedInAdminInfo(requestDTO));
    }

    private void saveLogs(AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {
        adminLogService.save(adminLogRequestDTO, httpServletRequest);
    }
}
