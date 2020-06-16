package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.commons.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AddressConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AddressConstant.FETCH_ZONE_LIST;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AddressConstants.BASE_ADDRESS;
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

    @GetMapping
    @ApiOperation(FETCH_ZONE_LIST)
    public ResponseEntity<?> fetchZoneDropDown() {
        return ok(addressService.fetchZoneDropDown());
    }
}
