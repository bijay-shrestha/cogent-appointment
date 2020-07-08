package com.cogent.cogentappointment.esewa.resource.v1;


import com.cogent.cogentappointment.commons.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AddressConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AddressConstants.*;
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

    @GetMapping(OLD + ZONE)
    @ApiOperation(FETCH_ZONE_LIST)
    public ResponseEntity<?> fetchZoneDropDown() {
        return ok(addressService.fetchZoneDropDown());
    }

    @GetMapping(OLD + DISTRICT + ZONE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DISTRICT_LIST_BY_ZONE_ID)
    public ResponseEntity<?> fetchDistrictDropDownByZoneId(@PathVariable("zoneId") Long zoneId) {
        return ok(addressService.fetchDistrictDropDownByZoneId(zoneId));
    }

    @GetMapping(OLD + STREET + DISTRICT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_STREET_LIST_BY_DISTRICT_ID)
    public ResponseEntity<?> fetchStreetDropDownByDistrictId(@PathVariable("districtId") Long districtId) {
        return ok(addressService.fetchStreetDropDownByDistrictId(districtId));
    }

    @GetMapping(NEW + PROVINCE)
    @ApiOperation(FETCH_PROVINCE_LIST)
    public ResponseEntity<?> fetchProvinceDropDown() {
        return ok(addressService.fetchProvinceDropDown());
    }


    @GetMapping(NEW + DISTRICT + PROVINCE_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DISTRICT_LIST_BY_PROVINCE_ID)
    public ResponseEntity<?> fetchDistrictDropDownByProvinceId(@PathVariable("provinceId") Long provinceId) {
        return ok(addressService.fetchDistrictDropDownByProvinceId(provinceId));
    }

    @GetMapping(NEW + MUNICIPALITY + DISTRICT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_MUNICIPALITY_LIST_BY_DISTRICT_ID)
    public ResponseEntity<?> fetchMunicipalityDropDownByDistrictId(@PathVariable("districtId") Long districtId) {
        return ok(addressService.fetchMunicipalityDropDownByDistrictId(districtId));
    }
}
