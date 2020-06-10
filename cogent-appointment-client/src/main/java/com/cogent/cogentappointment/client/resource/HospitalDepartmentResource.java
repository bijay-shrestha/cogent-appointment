package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.HospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.HospitalDepartmentConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DoctorConstants.BASE_DOCTOR;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.*;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/20/20
 */

@RestController
@RequestMapping(API_V1 + BASE_HOSPITAL_DEPARTMENT)
@Api(BASE_HOSPITAL_DEPARTMENT_API_VALUE)
public class HospitalDepartmentResource {

    private final HospitalDepartmentService hospitalDepartmentService;

    public HospitalDepartmentResource(HospitalDepartmentService hospitalDepartmentService) {
        this.hospitalDepartmentService = hospitalDepartmentService;
    }

    @PostMapping
    @ApiOperation(SAVE_HOSPITAL_DEPARTMENT_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody HospitalDepartmentRequestDTO requestDTO) {
        hospitalDepartmentService.save(requestDTO);
        return created(URI.create(API_V1 + BASE_HOSPITAL_DEPARTMENT)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_HOSPITAL_DEPARTMENT_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody HospitalDepartmentUpdateRequestDTO departmentUpdateRequestDTO) {
        hospitalDepartmentService.update(departmentUpdateRequestDTO);
        return ok().build();
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_HOSPITAL_DEPARTMENT_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchMinDepartment() {
        return ok(hospitalDepartmentService.fetchMinHospitalDepartment());
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchActiveMinDepartment() {
        return ok(hospitalDepartmentService.fetchActiveMinHospitalDepartment());
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_HOSPITAL_DEPARTMENT_OPERATION)
    public ResponseEntity<?> search(@RequestBody HospitalDepartmentSearchRequestDTO departmentSearchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok(hospitalDepartmentService.search(departmentSearchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(HOSPITAL_DEPARTMENT_DETAILS_OPERATION)
    public ResponseEntity<?> fetchHospitalDepartmentDetails(@PathVariable("id") Long id) {
        return ok(hospitalDepartmentService.fetchHospitalDepartmentDetails(id));
    }

    @DeleteMapping
    @ApiOperation(DELETE_HOSPITAL_DEPARTMENT_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        hospitalDepartmentService.delete(deleteRequestDTO);
        return ok().build();
    }

    @GetMapping(AVAILABLE + ROOM)
    @ApiOperation(FETCH_AVAILABLE_ROOM_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchAvailableRoom() {
        return ok(hospitalDepartmentService.fetchAvailableRoom());
    }

    @PutMapping(BILLING_MODE_WISE+CHARGE)
    @ApiOperation(FETCH_APPOINTMENT_CHARGE)
    public ResponseEntity<?> fetchAppointmentCharge(@Valid @RequestBody ChargeRequestDTO requestDTO) {
        return ok().body(hospitalDepartmentService.fetchAppointmentCharge(requestDTO));
    }

    @GetMapping(BASE_DOCTOR + HOSPITAL_DEPARTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ASSIGNED_DOCTOR)
    public ResponseEntity<?> fetchAppointmentCharge(@PathVariable("hospitalDepartmentId") Long hospitalDepartmentId) {
        return ok().body(hospitalDepartmentService.fetchAssignedHospitalDepartmentDoctor(hospitalDepartmentId));
    }
}
