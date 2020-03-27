package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminRequestDTO;
import com.cogent.cogentappointment.admin.service.CompanyAdminService;
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

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.CompanyAdminConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.CompanyAdminConstants.*;
import static java.net.URI.create;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = API_V1 + BASE_COMPANY_ADMIN)
@Api(BASE_API_VALUE)
public class CompanyAdminResource {

    private final CompanyAdminService companyAdminService;

    public CompanyAdminResource(CompanyAdminService companyAdminService) {
        this.companyAdminService = companyAdminService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("request") String request,
                                  HttpServletRequest httpServletRequest) throws IOException {

        CompanyAdminRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, CompanyAdminRequestDTO.class);
        companyAdminService.save(adminRequestDTO, file, httpServletRequest);
        return created(create(API_V1 + BASE_COMPANY_ADMIN)).build();
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveAdminsForDropdown() {
        return ok(companyAdminService.fetchActiveCompanyAdminsForDropdown());
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody CompanyAdminSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(companyAdminService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(companyAdminService.fetchCompanyAdminDetailsById(id));
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        companyAdminService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(CHANGE_PASSWORD)
    @ApiOperation(CHANGE_PASSWORD_OPERATION)
    public ResponseEntity<?> changePassword(@Valid @RequestBody AdminChangePasswordRequestDTO requestDTO) {
        companyAdminService.changePassword(requestDTO);
        return ok().build();
    }

    @PutMapping(RESET_PASSWORD)
    @ApiOperation(RESET_PASSWORD_OPERATION)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody AdminResetPasswordRequestDTO requestDTO) {
        companyAdminService.resetPassword(requestDTO);
        return ok().build();
    }

    @PutMapping(value = AVATAR, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_AVATAR_OPERATION)
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "files", required = false) MultipartFile file,
                                          @RequestParam("adminId") Long adminId) {
        companyAdminService.updateAvatar(file, adminId);
        return ok().build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("request") String request) throws IOException {

        CompanyAdminUpdateRequestDTO adminRequestDTO = ObjectMapperUtils.map(request, CompanyAdminUpdateRequestDTO.class);
        companyAdminService.update(adminRequestDTO, file);
        return ok().build();
    }

    @GetMapping(VERIFY)
    @ApiOperation(VERIFY_ADMIN)
    public ResponseEntity<?> verify(@RequestParam(name = "token") String token) {
        companyAdminService.verifyConfirmationToken(token);
        return ok().build();
    }

    @PostMapping(BASE_PASSWORD)
    @ApiOperation(SAVE_PASSWORD_OPERATION)
    public ResponseEntity<?> savePassword(@Valid @RequestBody AdminPasswordRequestDTO requestDTO) {
        companyAdminService.savePassword(requestDTO);
        return ok().build();
    }

    @GetMapping(COMPANY_ADMIN_META_INFO)
    @ApiOperation(FETCH_ADMIN_META_INFO)
    public ResponseEntity<?> fetchAdminMetaInfoDropdown() {
        return ok(companyAdminService.fetchCompanyAdminMetaInfoResponseDto());
    }

    @PutMapping(INFO)
    @ApiOperation(FETCH_LOGGED_IN_ADMIN_INFO)
    public ResponseEntity<?> fetchLoggedInAdminInfo(@Valid @RequestBody CompanyAdminInfoRequestDTO requestDTO) {
        return ok(companyAdminService.fetchLoggedInCompanyAdminInfo(requestDTO));
    }
}
