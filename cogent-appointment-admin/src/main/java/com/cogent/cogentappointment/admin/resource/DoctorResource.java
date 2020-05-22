package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorShiftRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.DoctorService;
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

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DoctorConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DoctorConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_WISE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ShiftConstants.BASE_SHIFT;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.SpecializationConstants.SPECIALIZATION_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.SpecializationConstants.SPECIALIZATION_WISE;
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
        DoctorRequestDTO requestDTO = ObjectMapperUtils.map(request, DoctorRequestDTO.class);
        doctorService.save(requestDTO, avatar);
        return created(create(API_V1 + BASE_DOCTOR)).build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                    @RequestParam("request") String request) throws IOException {
        DoctorUpdateRequestDTO updateRequestDTO = ObjectMapperUtils.map(request, DoctorUpdateRequestDTO.class);
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
        return ok(doctorService.fetchDoctorForDropdown());
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

    @GetMapping(HOSPITAL_WISE + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ACTIVE_DOCTORS_BY_HOSPITAL_ID)
    public ResponseEntity<?> fetchDoctorByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(doctorService.fetchDoctorByHospitalId(hospitalId));
    }

    @GetMapping(HOSPITAL_WISE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DOCTORS_BY_HOSPITAL_ID)
    public ResponseEntity<?> fetchMinDoctorByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(doctorService.fetchMinDoctorByHospitalId(hospitalId));
    }

    @GetMapping(BASE_SHIFT + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ASSIGNED_DOCTOR_SHIFTS)
    public ResponseEntity<?> fetchAssignedDoctorShifts(@PathVariable("doctorId") Long doctorId) {
        return ok(doctorService.fetchAssignedDoctorShifts(doctorId));
    }

    @PutMapping(BASE_SHIFT)
    @ApiOperation(ASSIGN_DOCTOR_SHIFTS)
    public ResponseEntity<?> assignShiftsToDoctor(@RequestBody DoctorShiftRequestDTO requestDTO){
        doctorService.assignShiftsToDoctor(requestDTO);
        return ok().build();
    }
}
