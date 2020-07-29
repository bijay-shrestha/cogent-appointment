package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DepartmentConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DepartmentConstants.BASE_DEPARTMENT;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti ON 25/01/2020
 */
@RestController
@RequestMapping(API_V1 + BASE_DEPARTMENT)
@Api(value = BASE_DEPARTMENT_API_VALUE)
public class DepartmentResource {

    private final DepartmentService departmentService;

    public DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @ApiOperation(SAVE_DEPARTMENT_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody DepartmentRequestDTO requestDTO) {
        departmentService.save(requestDTO);
        return created(URI.create(API_V1 + BASE_DEPARTMENT)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_DEPARTMENT_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody DepartmentUpdateRequestDTO departmentUpdateRequestDTO) {
        departmentService.update(departmentUpdateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_DEPARTMENT_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        departmentService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_DEPARTMENT_OPERATION)
    public ResponseEntity<?> search(@RequestBody DepartmentSearchRequestDTO departmentSearchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok(departmentService.search(departmentSearchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DEPARTMENT_DETAILS_OPERATION)
    public ResponseEntity<?> fetchDepartmentDetails(@PathVariable("id") Long id) {
        return ok(departmentService.fetchDetails(id));
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_DEPARTMENT_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchMinDepartment() {
        return ok(departmentService.fetchMinDepartment());
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_DEPARTMENT_FOR_DROP_DOWN_OPERATION)
    public ResponseEntity<?> fetchActiveMinDepartment() {
        return ok(departmentService.fetchActiveMinDepartment());
    }

}
