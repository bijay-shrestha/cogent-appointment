package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.HospitalDepartmentService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.HospitalDepartmentConstant.BASE_HOSPITAL_DEPARTMENT_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;

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
}
