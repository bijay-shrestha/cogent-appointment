package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.HospitalService;
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

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.HospitalConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 12/01/2020
 */
@RestController
@RequestMapping(API_V1 + BASE_HOSPITAL)
@Api(BASE_API_VALUE)
public class HospitalResource {

    private final HospitalService hospitalService;

    public HospitalResource(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestParam(value = "logo", required = false) MultipartFile logo,
                                  @RequestParam(value = "banner", required = false) MultipartFile banner,
                                  @RequestParam("request") String request) throws IOException, NoSuchAlgorithmException {

        HospitalRequestDTO requestDTO = ObjectMapperUtils.map(request, HospitalRequestDTO.class);
        hospitalService.save(requestDTO, logo, banner);
        return created(create(API_V1 + BASE_HOSPITAL)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "logo", required = false) MultipartFile logo,
                                    @RequestParam(value = "banner", required = false) MultipartFile banner,
                                    @RequestParam("request") String request) throws IOException  {

        HospitalUpdateRequestDTO updateRequestDTO = ObjectMapperUtils.map(request, HospitalUpdateRequestDTO.class);
        hospitalService.update(updateRequestDTO, logo, banner);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        hospitalService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody HospitalSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDown() {
        return ok(hospitalService.fetchHospitalForDropDown());
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(hospitalService.fetchDetailsById(id));
    }
}
