package com.cogent.cogentappointment.admin.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;

/**
 * @author smriti on 25/03/20
 */
@RestController
@RequestMapping(API_V1)
@Api("hello")
public class TestResource {

    @GetMapping("/hello")
    public String test() {
        return "hello world";
    }
}
