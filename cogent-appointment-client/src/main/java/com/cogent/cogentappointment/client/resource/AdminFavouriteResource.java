package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.service.AdminFavouriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminFavouriteConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AdminFavouriteConstants.BASE_ADMIN_FAVOURITE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak ON 2020/06/16-12:13 PM
 */
@RestController
@RequestMapping(value = API_V1 + BASE_ADMIN_FAVOURITE)
@Api(BASE_API_VALUE)
public class AdminFavouriteResource {

    private final AdminFavouriteService adminFavouriteService;

    public AdminFavouriteResource(AdminFavouriteService adminFavouriteService) {
        this.adminFavouriteService = adminFavouriteService;
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDown() {
        return ok(adminFavouriteService.fetchAdminFavouriteForDropDown());
    }

}
