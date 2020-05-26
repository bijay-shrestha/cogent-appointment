package com.cogent.cogentappointment.admin.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentServiceTypeConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentServiceTypeConstants.BASE_APPOINTMENT_SERVICE_TYPE;

/**
 * @author smriti on 26/05/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT_SERVICE_TYPE)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentServiceTypeResource {
}
