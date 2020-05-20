package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.service.HospitalDepartmentDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SpecializationDutyRosterConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SpecializationDutyRosterConstant.SAVE_OPERATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDeptDutyRosterConstants.BASE_HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;

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
}
