package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.PatientConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.PatientConstant.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.getPageable;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RestController
@RequestMapping(API_V1 + BASE_PATIENT)
@Api(value = BASE_PATIENT_API_VALUE)
public class PatientResource {

    private final PatientService patientService;

    public PatientResource(PatientService patientService) {
        this.patientService = patientService;
    }

    @PutMapping(SEARCH + SELF)
    @ApiOperation(SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION)
    public ResponseEntity<?> searchForSelf(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO) {
        return ok(patientService.searchForSelf(searchRequestDTO));
    }

    @PutMapping(SEARCH + OTHERS)
    @ApiOperation(SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
    public ResponseEntity<?> search(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        return ok(patientService.fetchMinimalPatientInfo(searchRequestDTO, getPageable(page, size)));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_BY_ID)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(patientService.fetchDetailsById(id));
    }

//    @GetMapping(DETAILS + ID_PATH_VARIABLE_BASE)
//    @ApiOperation(PATIENT_DETAILS_OPERATION)
//    public ResponseEntity<?> fetchPatientDetails(@PathVariable Long id) {
//        return ok(patientService.search(id));
//    }
//
//    @GetMapping(DROPDOWN)
//    @ApiOperation(FETCH_PATIENT_FOR_DROP_DOWN_OPERATION)
//    public ResponseEntity<?> dropDown() {
//        return ok(patientService.dropDownList());
//    }
//
//    @GetMapping(ACTIVE + DROPDOWN)
//    @ApiOperation(FETCH_ACTIVE_PATIENT_FOR_DROP_DOWN_OPERATION)
//    public ResponseEntity<?> activeDropDown() {
//        return ok(patientService.activeDropDownList());
//    }
}
