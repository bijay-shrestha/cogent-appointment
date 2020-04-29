package com.cogent.cogentappointment.client.resource;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.TestConstant.TEST_OPERATION;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.TestConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.TestResourceConstant.BASE_TEST_RESOURCE;

@RestController
@RequestMapping(value = API_V1 + BASE_TEST_RESOURCE)
@Api(BASE_API_VALUE)
public class TestResource {


    @GetMapping
    @ApiOperation(TEST_OPERATION)
    public String testClient(){
        return "e-Appointments client is running successfully! ....";
    }
}
