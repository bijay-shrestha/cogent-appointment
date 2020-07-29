package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.SpecializationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.SpecializationConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DoctorConstants.DOCTOR_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DoctorConstants.DOCTOR_WISE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_WISE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.SpecializationConstants.BASE_SPECIALIZATION;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-09-25
 */
@RestController
@RequestMapping(API_V1 + BASE_SPECIALIZATION)
@Api(BASE_API_VALUE)
public class SpecializationResource {

    private final SpecializationService specializationService;

    public SpecializationResource(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody SpecializationRequestDTO requestDTO) {
        specializationService.save(requestDTO);
        return created(create(API_V1 + BASE_SPECIALIZATION)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody SpecializationUpdateRequestDTO updateRequestDTO) {
        specializationService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        specializationService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody SpecializationSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(specializationService.search(searchRequestDTO, pageable));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveSpecializationForDropDown() {
        return ok(specializationService.fetchActiveSpecializationForDropDown());
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchSpecializationForDropDown() {
        return ok(specializationService.fetchSpecializationForDropDown());
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(specializationService.fetchDetailsById(id));
    }

    @GetMapping(DOCTOR_WISE + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BY_DOCTOR_ID)
    public ResponseEntity<?> fetchActiveSpecializationByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return ok(specializationService.fetchActiveSpecializationByDoctorId(doctorId));
    }

    @GetMapping(DOCTOR_WISE + MIN + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BY_DOCTOR_ID)
    public ResponseEntity<?> fetchSpecializationByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return ok(specializationService.fetchSpecializationByDoctorId(doctorId));
    }

    @GetMapping(HOSPITAL_WISE + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BY_HOSPITAL_ID)
    public ResponseEntity<?> fetchActiveSpecializationByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(specializationService.fetchActiveSpecializationByHospitalId(hospitalId));
    }

    @GetMapping(HOSPITAL_WISE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_BY_HOSPITAL_ID)
    public ResponseEntity<?> fetchSpecializationByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(specializationService.fetchSpecializationByHospitalId(hospitalId));
    }
}
