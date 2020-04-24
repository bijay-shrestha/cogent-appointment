package com.cogent.cogentappointment.logging.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestResource {

    @GetMapping("/run")
    public String test(){
        return "Logging Server is running fine.";
    }
}
