package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.CountryConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.CountryConstants.BASE_COUNTRY;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.MIN;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 08/11/2019
 */
@RestController
@RequestMapping(API_V1 + BASE_COUNTRY)
@Api(BASE_API_VALUE)
public class CountryResource {

    private final CountryService countryService;

    public CountryResource(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @ApiOperation(FETCH_ACTIVE_COUNTRY)
    public ResponseEntity<?> fetchActiveCountry() {
        return ok(countryService.fetchActiveCountry());
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_COUNTRY)
    public ResponseEntity<?> fetchCountry() {
        return ok(countryService.fetchCountry());
    }
}
