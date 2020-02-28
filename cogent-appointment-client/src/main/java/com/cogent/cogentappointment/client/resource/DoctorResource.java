package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.DoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DoctorConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DoctorConstants.BASE_DOCTOR;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DoctorConstants.UPDATE_DETAILS;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_WISE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SpecializationConstants.SPECIALIZATION_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SpecializationConstants.SPECIALIZATION_WISE;
import static com.cogent.cogentappointment.client.utils.commons.ObjectMapperUtils.map;
import static java.net.URI.create;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-09-29
 */
@RestController
@RequestMapping(value = API_V1 + BASE_DOCTOR)
@Api(BASE_API_VALUE)
public class DoctorResource {
    private final DoctorService doctorService;

    public DoctorResource(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                  @RequestParam("request") String request) throws IOException {
        DoctorRequestDTO requestDTO = map(request, DoctorRequestDTO.class);
        doctorService.save(requestDTO, avatar);
        return created(create(API_V1 + BASE_DOCTOR)).build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                    @RequestParam("request") String request) throws IOException {
        DoctorUpdateRequestDTO updateRequestDTO = map(request, DoctorUpdateRequestDTO.class);
        doctorService.update(updateRequestDTO, avatar);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        doctorService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody DoctorSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(doctorService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchDoctorForDropDown() {
        return ok(doctorService.fetchActiveMinDoctor());
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(doctorService.fetchDetailsById(id));
    }

    @GetMapping(UPDATE_DETAILS + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_FOR_UPDATE_MODAL_OPERATION)
    public ResponseEntity<?> fetchDetailsForUpdate(@PathVariable("id") Long id) {
        return ok(doctorService.fetchDetailsForUpdate(id));
    }

    @GetMapping(SPECIALIZATION_WISE + SPECIALIZATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BY_SPECIALIZATION_ID)
    public ResponseEntity<?> fetchDoctorBySpecializationId(@PathVariable("specializationId") Long specializationId) {
        return ok(doctorService.fetchDoctorBySpecializationId(specializationId));
    }

    @GetMapping(HOSPITAL_WISE )
    @ApiOperation(FETCH_BY_HOSPITAL_ID)
    public ResponseEntity<?> fetchDoctorByHospitalId() {
        return ok(doctorService.fetchDoctorByHospitalId());
    }

}
