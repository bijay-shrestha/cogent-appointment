package com.cogent.cogentappointment.client.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AppointmentTransferConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;

/**
 * @author Sauravi Thapa ON 5/6/20
 */

@RequestMapping(API_V1)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentTransferResource  {
}
