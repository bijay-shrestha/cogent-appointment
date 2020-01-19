package com.cogent.cogentappointment.resource;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.service.HospitalService;
import com.cogent.cogentappointment.utils.commons.ObjectMapperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static com.cogent.cogentappointment.constants.SwaggerConstants.HospitalConstant.*;
import static com.cogent.cogentappointment.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.constants.WebResourceKeyConstants.HospitalConstants.BASE_HOSPITAL;
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
    public ResponseEntity<?> save(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("request") String request) throws IOException {

        HospitalRequestDTO requestDTO = ObjectMapperUtils.map(request, HospitalRequestDTO.class);
        hospitalService.save(requestDTO, file);
        return created(create(API_V1 + BASE_HOSPITAL)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("request") String request) throws IOException {

        HospitalUpdateRequestDTO updateRequestDTO = ObjectMapperUtils.map(request, HospitalUpdateRequestDTO.class);
        hospitalService.update(updateRequestDTO, file);
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

    @GetMapping(ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BY_ID)
    public ResponseEntity<?> fetchHospital(@PathVariable("id") Long id) {
        return ok(hospitalService.fetchActiveHospital(id));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDown() {
        return ok(hospitalService.fetchHospitalForDropDown());
    }

    @GetMapping(DETAILS + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(hospitalService.fetchDetailsById(id));
    }
}
