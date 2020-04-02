package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.CompanyProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.CompanyProfileConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.CompanyConstants.COMPANY_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.CompanyProfileConstants.BASE_COMPANY_PROFILE;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 7/2/19
 */
@RestController
@RequestMapping(value = API_V1 + BASE_COMPANY_PROFILE)
@Api(BASE_API_VALUE)
public class CompanyProfileResource {

    private final CompanyProfileService companyProfileService;

    public CompanyProfileResource(CompanyProfileService companyProfileService) {
        this.companyProfileService = companyProfileService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody CompanyProfileRequestDTO requestDTO) {
        companyProfileService.save(requestDTO);
        return created(create(API_V1 + BASE_COMPANY_PROFILE)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<Void> update(@Valid @RequestBody CompanyProfileUpdateRequestDTO requestDTO) {
        companyProfileService.update(requestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        companyProfileService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody CompanyProfileSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(companyProfileService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(companyProfileService.fetchDetailsById(id));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchMinActiveCompanyProfile() {
        return ok(companyProfileService.fetchMinActiveCompanyProfile());
    }

    @GetMapping(ACTIVE + MIN + COMPANY_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_MIN_DETAILS_BY_COMPANY_ID)
    public ResponseEntity<?> fetchMinActiveCompanyProfile(@PathVariable("companyId") Long companyId) {
        return ok(companyProfileService.fetchMinActiveCompanyProfileByCompanyId(companyId));
    }
}