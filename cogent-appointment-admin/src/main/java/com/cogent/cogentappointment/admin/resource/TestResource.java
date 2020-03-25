package com.cogent.cogentappointment.admin.resource;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author smriti on 25/03/20
 */
@RestController
@RequestMapping
@Api("Test")
public class TestResource {

    @GetMapping("/test")
    public String test() {
        System.out.println(":::: SERVER IS RUNNING ::::");
        return "test done";
    }
}
