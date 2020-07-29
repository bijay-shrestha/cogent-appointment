package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.favourite.AdminFavouriteSaveRequestDTO;
import com.cogent.cogentappointment.client.dto.request.favourite.AdminFavouriteUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.AdminFavouriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminFavouriteConstant.*;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminFeatureConstant.FAVOURITE_DETAILS_BY_ADMIN_ID;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AdminFavouriteConstants.BASE_ADMIN_FAVOURITE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.ID_PATH_VARIABLE_BASE;
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

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody AdminFavouriteUpdateRequestDTO requestDTO) {
        adminFavouriteService.update(requestDTO);
        return ok().build();
    }

    @GetMapping(ID_PATH_VARIABLE_BASE)
    @ApiOperation(FAVOURITE_DETAILS_BY_ADMIN_ID)
    public ResponseEntity<?> getAdminFavouriteByAdminId(@PathVariable("id") Long id) {
        return ok(adminFavouriteService.getAdminFavouriteByAdminId(id));
    }



//    @GetMapping(ACTIVE + MIN)
//    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
//    public ResponseEntity<?> fetchHospitalForDropDown() {
//        return ok(adminFavouriteService.fetchAdminFavouriteForDropDown());
//    }
//
//    @GetMapping(ICON + ACTIVE + MIN)
//    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
//    public ResponseEntity<?> fetchHospitalForDropDownWithIcon() {
//        return ok(adminFavouriteService.fetchAdminFavouriteForDropDownWithIcon());
//    }


}
