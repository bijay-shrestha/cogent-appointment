package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.admin.service.CompanyService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.CompanyConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.CompanyConstants.BASE_COMPANY;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping(API_V1 + BASE_COMPANY)
@RestController
@Api(BASE_API_VALUE)
public class CompanyResource {

    private final CompanyService companyService;

    public CompanyResource(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestParam(value = "logo", required = false) MultipartFile logo,
                                  @RequestParam("request") String request) throws IOException, NoSuchAlgorithmException {

        CompanyRequestDTO requestDTO = ObjectMapperUtils.map(request, CompanyRequestDTO.class);
        companyService.save(requestDTO, logo);
        return created(create(API_V1 + BASE_COMPANY)).build();
    }
    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "logo", required = false) MultipartFile logo,
                                    @RequestParam("request") String request) throws IOException, NoSuchAlgorithmException {

        CompanyUpdateRequestDTO updateRequestDTO = ObjectMapperUtils.map(request, CompanyUpdateRequestDTO.class);
        companyService.update(updateRequestDTO, logo);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        companyService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody CompanySearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(companyService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDown() {
        return ok(companyService.fetchCompanyForDropDown());
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<CompanyResponseDTO> fetchDetailsById(@PathVariable("id") Long id) {
        CompanyResponseDTO responseDTO = companyService.fetchDetailsById(id);
        return ok().body(responseDTO);
    }
}
