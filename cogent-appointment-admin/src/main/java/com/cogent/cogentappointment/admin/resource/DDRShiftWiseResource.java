package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.service.DDRShiftWiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DoctorDutyRosterShiftWiseConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DDRShiftWiseConstants.BASE_DDR_SHIFT_WISE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DDRShiftWiseConstants.OVERRIDE;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;

/**
 * @author smriti on 08/05/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_DDR_SHIFT_WISE)
@Api(BASE_API_VALUE)
public class DDRShiftWiseResource {

    private final DDRShiftWiseService ddrShiftWiseService;

    public DDRShiftWiseResource(DDRShiftWiseService ddrShiftWiseService) {
        this.ddrShiftWiseService = ddrShiftWiseService;
    }

    @PostMapping
    @ApiOperation(SAVE_WEEK_DAYS_ROSTER_OPERATION)
    public ResponseEntity<?> saveDDRWeekDaysDetail(@Valid @RequestBody DDRRequestDTO requestDTO) {
        ddrShiftWiseService.saveDDRWeekDaysDetail(requestDTO);
        return created(create(API_V1 + BASE_DDR_SHIFT_WISE)).build();
    }

    @PostMapping(OVERRIDE)
    @ApiOperation(SAVE_OVERRIDE_ROSTER_OPERATION)
    public ResponseEntity<?> saveDDROverrideDetail (@RequestBody DDROverrideRequestDTO requestDTO){
        ddrShiftWiseService.saveDDROverrideDetail(requestDTO);
        return created(create(API_V1 + BASE_DDR_SHIFT_WISE+OVERRIDE)).build();
    }
}
