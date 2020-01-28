package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.admin.service.SpecializationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-09-25
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.SpecializationConstants.BASE_SPECIALIZATION)
@Api(SwaggerConstants.SpecializationConstant.BASE_API_VALUE)
public class SpecializationResource {

    private final SpecializationService specializationService;

    public SpecializationResource(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    @ApiOperation(SwaggerConstants.SpecializationConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody SpecializationRequestDTO requestDTO) {
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.SpecializationConstants.BASE_SPECIALIZATION)).body(specializationService.save(requestDTO));
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.SpecializationConstant.UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody SpecializationUpdateRequestDTO updateRequestDTO) {
        specializationService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.SpecializationConstant.DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        specializationService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.SpecializationConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody SpecializationSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(specializationService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.SpecializationConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveSpecializationForDropDown() {
        return ok(specializationService.fetchActiveSpecializationForDropDown());
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.SpecializationConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(specializationService.fetchDetailsById(id));
    }

    @GetMapping(WebResourceKeyConstants.DoctorConstants.DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.SpecializationConstant.FETCH_BY_DOCTOR_ID)
    public ResponseEntity<?> fetchSpecializationByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return ok(specializationService.fetchSpecializationByDoctorId(doctorId));
    }
}
