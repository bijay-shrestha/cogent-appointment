package com.cogent.cogentappointment.client.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.SpecializationDutyRosterConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.SpecializationDutyRosterConstants.BASE_SPECIALIZATION_DUTY_ROSTER;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_SPECIALIZATION_DUTY_ROSTER)
@Api(BASE_API_VALUE)
public class SpecializationDutyRosterResource {
}
