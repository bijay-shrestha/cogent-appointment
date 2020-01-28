package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 12/01/2020
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL)
@Api(SwaggerConstants.HospitalConstant.BASE_API_VALUE)
public class HospitalResource {

    private final HospitalService hospitalService;

    public HospitalResource(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping
    @ApiOperation(SwaggerConstants.HospitalConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("request") String request) throws IOException {

        HospitalRequestDTO requestDTO = ObjectMapperUtils.map(request, HospitalRequestDTO.class);
        hospitalService.save(requestDTO, file);
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL)).build();
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.HospitalConstant.UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("request") String request) throws IOException {

        HospitalUpdateRequestDTO updateRequestDTO = ObjectMapperUtils.map(request, HospitalUpdateRequestDTO.class);
        hospitalService.update(updateRequestDTO, file);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.HospitalConstant.DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        hospitalService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.HospitalConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody HospitalSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.HospitalConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDown() {
        return ok(hospitalService.fetchHospitalForDropDown());
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.HospitalConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(hospitalService.fetchDetailsById(id));
    }
}
