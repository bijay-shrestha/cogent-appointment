package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.CompanyService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.CompanyConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.CompanyConstants.BASE_COMPANY;

@RequestMapping(API_V1 + BASE_COMPANY)
@RestController
@Api(BASE_API_VALUE)
public class CompanyResource {
    private final CompanyService companyService;

    public CompanyResource(CompanyService companyService) {
        this.companyService = companyService;
    }
}
