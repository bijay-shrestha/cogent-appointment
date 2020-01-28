package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.admin.service.DoctorDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;


/**
 * @author smriti on 26/11/2019
 */
@RestController
@RequestMapping(value = WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.DoctorDutyRosterConstants.BASE_DOCTOR_DUTY_ROSTER)
@Api(SwaggerConstants.DoctorDutyRosterConstant.BASE_API_VALUE)
public class DoctorDutyRosterResource {

    private final DoctorDutyRosterService doctorDutyRosterService;

    public DoctorDutyRosterResource(DoctorDutyRosterService doctorDutyRosterService) {
        this.doctorDutyRosterService = doctorDutyRosterService;
    }

    @PostMapping
    @ApiOperation(SwaggerConstants.DoctorDutyRosterConstant.SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody DoctorDutyRosterRequestDTO requestDTO) {
        doctorDutyRosterService.save(requestDTO);
        return created(create(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.DoctorDutyRosterConstants.BASE_DOCTOR_DUTY_ROSTER)).build();
    }

    @PutMapping
    @ApiOperation(SwaggerConstants.DoctorDutyRosterConstant.UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody DoctorDutyRosterUpdateRequestDTO updateRequestDTO) {
        doctorDutyRosterService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(SwaggerConstants.DoctorDutyRosterConstant.DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        doctorDutyRosterService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(WebResourceKeyConstants.SEARCH)
    @ApiOperation(SwaggerConstants.DoctorDutyRosterConstant.SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(doctorDutyRosterService.search(searchRequestDTO, pageable));
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.DoctorDutyRosterConstant.DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(doctorDutyRosterService.fetchDetailsById(id));
    }

//
//    @PutMapping(DOCTOR_DUTY_ROSTER_STATUS)
//    @ApiOperation(FETCH_DOCTOR_DUTY_ROSTER_STATUS_OPERATION)
//    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(
//            @RequestBody DoctorDutyRosterStatusRequestDTO searchRequestDTO) {
//        return doctorDutyRosterService.fetchDoctorDutyRosterStatus(searchRequestDTO);
//    }
}


