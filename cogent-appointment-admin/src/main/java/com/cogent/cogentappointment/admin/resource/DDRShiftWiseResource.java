package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.DDRRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.DDRShiftRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.service.DDRShiftWiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DoctorDutyRosterShiftWiseConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.DoctorDutyRosterShiftWiseConstant.SAVE_OPERATION;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.DDRShiftWiseConstants.BASE_DDR_SHIFT_WISE;
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
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody DDRRequestDTO requestDTO) {
//        doctorDutyRosterService.save(requestDTO);
        return created(create(API_V1 + BASE_DDR_SHIFT_WISE)).build();
    }
}
