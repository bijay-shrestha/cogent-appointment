package com.cogent.cogentappointment.esewa.resource;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.TestConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.TestConstant.TEST_OPERATION;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.TestResourceConstant.BASE_TEST_RESOURCE;

@RestController
@RequestMapping(value = API_V1 + BASE_TEST_RESOURCE)
@Api(BASE_API_VALUE)
public class TestResource {

    @GetMapping
    @ApiOperation(TEST_OPERATION)
    public String testClient(){
        return "e-Appointment esewa module is running successfully in Kubeshpere! ....";
    }
}
