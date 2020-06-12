package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.HospitalDepartmentLogsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.HospitalDepartmentAppointmentLogConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;

/**
 * @author Sauravi Thapa ON 6/12/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class HospitalDepartmentLogsResource {

    private final HospitalDepartmentLogsService hospitalDepartmentLogsService;

    public HospitalDepartmentLogsResource(HospitalDepartmentLogsService hospitalDepartmentLogsService) {
        this.hospitalDepartmentLogsService = hospitalDepartmentLogsService;
    }

}
