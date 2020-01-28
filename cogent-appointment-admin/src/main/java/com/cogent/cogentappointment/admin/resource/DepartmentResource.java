package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.DepartmentService;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 25/01/2020
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.DepartmentConstants.BASE_DEPARTMENT)
@Api(value = SwaggerConstants.DepartmentConstant.BASE_DEPARTMENT_API_VALUE)
public class DepartmentResource {

    private final DepartmentService departmentService;

    public DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @ApiOperation(SwaggerConstants.DepartmentConstant.SAVE_DEPARTMENT_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        departmentService.save(requestDTO);
        return created(URI.create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.DepartmentConstants.BASE_DEPARTMENT)).build();
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.DepartmentConstant.UPDATE_DEPARTMENT_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody DepartmentUpdateRequestDTO departmentUpdateRequestDTO) {
        departmentService.update(departmentUpdateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.DepartmentConstant.DELETE_DEPARTMENT_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        departmentService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.DepartmentConstant.SEARCH_DEPARTMENT_OPERATION)
    public ResponseEntity<?> search(@RequestBody DepartmentSearchRequestDTO departmentSearchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok(departmentService.search(departmentSearchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.DepartmentConstant.DEPARTMENT_DETAILS_OPERATION)
    public ResponseEntity<?> fetchDepartmentDetails(@PathVariable("id") Long id) {
        return ok(departmentService.fetchDetails(id));
    }

    @GetMapping(WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.DepartmentConstant.FETCH_DEPARTMENT_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchDropDownList() {
        return ok(departmentService.fetchDepartmentForDropdown());
    }

    @GetMapping(WebResourceKeyConstants.ACTIVE + WebResourceKeyConstants.MIN)
    @ApiOperation(SwaggerConstants.DepartmentConstant.FETCH_ACTIVE_DEPARTMENT_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchActiveDropDownList() {
        return ok(departmentService.fetchActiveDropDownList());
    }

    @GetMapping(WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.DepartmentConstant.FETCH_DEPARTMENT_BY_HOSPITAL_OPERATION)
    public ResponseEntity<?> fetchDepartmentByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(departmentService.fetchDepartmentByHospitalId(hospitalId));
    }
}
