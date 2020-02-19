package com.cogent.cogentappointment.admin.resource;


import io.swagger.annotations.Api;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.SpecializationConstant.BASE_API_VALUE;

@RestController
@Api(BASE_API_VALUE)
public class TestCacheController {

    @GetMapping("/test/{id}")
    public String findById(@PathVariable String id){
        System.out.println("Searching by ID  : " + id);
        try {
            Thread.sleep(1000*5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Sauravi";
    }
}
