package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.HospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.HospitalDepartmentConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.MIN;
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
}
