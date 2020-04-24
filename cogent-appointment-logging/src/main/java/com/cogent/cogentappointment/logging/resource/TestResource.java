package com.cogent.cogentappointment.logging.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.AdminLogConstant.TEST_OPERATION;
import static com.cogent.cogentappointment.logging.constants.SwaggerConstants.AdminLogConstant.TEST_RESOURCE;

@RestController
@RequestMapping(value = "/test")
@Api(TEST_RESOURCE)
public class TestResource {

    @GetMapping
    @ApiOperation(TEST_OPERATION)
    public String test(){
        return "Logging Server is running fine.";
    }
}
