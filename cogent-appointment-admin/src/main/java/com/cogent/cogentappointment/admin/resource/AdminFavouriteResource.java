package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.dto.request.favourite.AdminFavouriteSaveRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminFavouriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AdminFavouriteConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AdminFavouriteConstant.SAVE_ADMIN_FAVOURITE_OPERATION;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.CompanyConstant.FETCH_DETAILS_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AdminFavouriteConstants.BASE_ADMIN_FAVOURITE;
import static org.springframework.http.ResponseEntity.created;
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

    @PostMapping
    @ApiOperation(SAVE_ADMIN_FAVOURITE_OPERATION)
    public ResponseEntity<?> save(@RequestBody AdminFavouriteSaveRequestDTO adminFavouriteSaveRequestDTO) {
        adminFavouriteService.save(adminFavouriteSaveRequestDTO);
        return created(URI.create(API_V1 + BASE_ADMIN_FAVOURITE)).build();
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDown() {
        return ok(adminFavouriteService.fetchAdminFavouriteForDropDown());
    }

    @GetMapping(ICON + ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchHospitalForDropDownWithIcon() {
        return ok(adminFavouriteService.fetchAdminFavouriteForDropDownWithIcon());
    }


}
