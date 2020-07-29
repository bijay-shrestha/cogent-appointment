package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.service.SalutationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.SalutationConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.SalutationConstant.BASE_SALUTATION;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(API_V1 + BASE_SALUTATION)
@Api(BASE_API_VALUE)
public class SalutationResource {

    private final SalutationService salutationService;

    public SalutationResource(SalutationService salutationService) {
        this.salutationService = salutationService;
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchSalutationForDropDown() {
        return ok(salutationService.fetchActiveMinSalutation());
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        salutationService.delete(deleteRequestDTO);
        return ok().build();
    }


}
