package com.cogent.cogentappointment.admin.resource;


import com.cogent.cogentappointment.commons.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AddressConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AddressConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 6/15/20
 */

@RestController
@RequestMapping(value = API_V1 + BASE_ADDRESS)
@Api(BASE_API_VALUE)
public class AddressResource {

    private final AddressService addressService;

    public AddressResource(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(ZONE)
    @ApiOperation(FETCH_ZONE_LIST)
    public ResponseEntity<?> fetchZoneDropDown() {
        return ok(addressService.fetchZoneDropDown());
    }

    @GetMapping(PROVINCE)
    @ApiOperation(FETCH_PROVINCE_LIST)
    public ResponseEntity<?> fetchProvinceDropDown() {
        return ok(addressService.fetchProvinceDropDown());
    }

    @GetMapping(ZONE + DISTRICT + ZONE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DISTRICT_LIST_BY_ZONE_ID)
    public ResponseEntity<?> fetchDistrictDropDownByZoneId(@PathVariable("zoneId") BigInteger zoneId) {
        return ok(addressService.fetchDistrictDropDownByZoneId(zoneId));
    }

    @GetMapping(PROVINCE + DISTRICT + PROVINCE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DISTRICT_LIST_BY_PROVINCE_ID)
    public ResponseEntity<?> fetchDistrictDropDownByProvinceId(@PathVariable("provinceId") BigInteger provinceId) {
        return ok(addressService.fetchDistrictDropDownByProvinceId(provinceId));
    }
}
