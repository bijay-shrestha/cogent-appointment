package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterUpdateRequestDTO;
import com.cogent.cogentappointment.client.service.DoctorDutyRosterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.DoctorDutyRosterConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.DoctorDutyRosterConstants.BASE_DOCTOR_DUTY_ROSTER;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;


/**
 * @author smriti on 26/11/2019
 */
@RestController
@RequestMapping(value = API_V1 + BASE_DOCTOR_DUTY_ROSTER)
@Api(BASE_API_VALUE)
public class DoctorDutyRosterResource {

    private final DoctorDutyRosterService doctorDutyRosterService;

    public DoctorDutyRosterResource(DoctorDutyRosterService doctorDutyRosterService) {
        this.doctorDutyRosterService = doctorDutyRosterService;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody DoctorDutyRosterRequestDTO requestDTO) {
        doctorDutyRosterService.save(requestDTO);
        return created(create(API_V1 + BASE_DOCTOR_DUTY_ROSTER)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody DoctorDutyRosterUpdateRequestDTO updateRequestDTO) {
        doctorDutyRosterService.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        doctorDutyRosterService.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(doctorDutyRosterService.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
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


