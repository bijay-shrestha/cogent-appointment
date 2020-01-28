package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 08/11/2019
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.CountryConstants.BASE_COUNTRY)
@Api(SwaggerConstants.CountryConstant.BASE_API_VALUE)
public class CountryResource {

    private final CountryService countryService;

    public CountryResource(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @ApiOperation(SwaggerConstants.CountryConstant.FETCH_ACTIVE_COUNTRY)
    public ResponseEntity<?> fetchActiveCountry() {
        return ok(countryService.fetchActiveCountry());
    }
}
