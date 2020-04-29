package com.cogent.cogentappointment.client.resource;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestResource {


    @GetMapping
    public String testClient(){
        return "e-Appointments client is running successfully! ....";
    }
}
