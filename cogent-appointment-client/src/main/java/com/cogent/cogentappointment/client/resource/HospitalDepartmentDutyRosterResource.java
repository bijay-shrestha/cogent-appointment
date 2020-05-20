package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.service.HospitalDepartmentDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.HospitalDeptDutyRosterConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDeptDutyRosterConstants.BASE_HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SEARCH;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_HOSPITAL_DEPARTMENT_DUTY_ROSTER)
@Api(BASE_API_VALUE)
public class HospitalDepartmentDutyRosterResource {

    private final HospitalDepartmentDutyRosterService hospitalDepartmentDutyRosterService;

    public HospitalDepartmentDutyRosterResource(HospitalDepartmentDutyRosterService hospitalDepartmentDutyRosterService) {
        this.hospitalDepartmentDutyRosterService = hospitalDepartmentDutyRosterService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody HospitalDepartmentDutyRosterRequestDTO requestDTO) {
        hospitalDepartmentDutyRosterService.save(requestDTO);
        return created(create(API_V1 + BASE_HOSPITAL_DEPARTMENT_DUTY_ROSTER)).build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalDepartmentDutyRosterService.search(searchRequestDTO, pageable));
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        hospitalDepartmentDutyRosterService.delete(deleteRequestDTO);
        return ok().build();
    }
}
