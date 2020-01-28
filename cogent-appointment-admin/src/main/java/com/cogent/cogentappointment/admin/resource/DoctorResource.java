package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.admin.service.DoctorService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorUpdateRequestDTO;
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
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-09-29
 */
@RestController
@RequestMapping(value = WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.DoctorConstants.BASE_DOCTOR)
@Api(SwaggerConstants.DoctorConstant.BASE_API_VALUE)
public class DoctorResource {
    private final DoctorService doctorService;

    public DoctorResource(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SwaggerConstants.DoctorConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                  @RequestParam("request") String request) throws IOException {
        DoctorRequestDTO requestDTO = ObjectMapperUtils.map(request, DoctorRequestDTO.class);
        doctorService.save(requestDTO, avatar);
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.DoctorConstants.BASE_DOCTOR)).build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(SwaggerConstants.DoctorConstant.UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestPart(value = "file", required = false) MultipartFile avatar,
                                    @RequestParam("request") String request) throws IOException {
        DoctorUpdateRequestDTO updateRequestDTO = ObjectMapperUtils.map(request, DoctorUpdateRequestDTO.class);
        doctorService.update(updateRequestDTO, avatar);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.DoctorConstant.DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        doctorService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.DoctorConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody DoctorSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(doctorService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.DoctorConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchDoctorForDropDown() {
        return ok(doctorService.fetchDoctorForDropdown());
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.DoctorConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(doctorService.fetchDetailsById(id));
    }

    @GetMapping(WebResourceKeyConstants.DoctorConstants.UPDATE_DETAILS + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.DoctorConstant.DETAILS_FOR_UPDATE_MODAL_OPERATION)
    public ResponseEntity<?> fetchDetailsForUpdate(@PathVariable("id") Long id) {
        return ok(doctorService.fetchDetailsForUpdate(id));
    }

    @GetMapping(WebResourceKeyConstants.SpecializationConstants.SPECIALIZATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.DoctorConstant.FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchDoctorBySpecializationId(@PathVariable("specializationId") Long specializationId) {
        return ok(doctorService.fetchDoctorBySpecializationId(specializationId));
    }

}
