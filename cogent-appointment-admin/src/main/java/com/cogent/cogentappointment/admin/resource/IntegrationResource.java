package com.cogent.cogentappointment.admin.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.IntegrationConstants.BASE_INTEGRATION;

/**
 * @author rupak on 2020-05-18
 */
@RestController
@RequestMapping(API_V1 + BASE_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationResource {
}
