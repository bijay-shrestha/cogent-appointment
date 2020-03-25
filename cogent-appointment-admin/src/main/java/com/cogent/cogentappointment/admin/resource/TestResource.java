package com.cogent.cogentappointment.admin.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author smriti on 25/03/20
 */
@RestController
@RequestMapping(value = "/test")
public class TestResource {

    @GetMapping
    public String test() {
        System.out.println(":::: SERVER IS RUNNING ::::");
        return "test done";
    }
}
