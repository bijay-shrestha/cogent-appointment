package com.cogent.cogentappointment.resource;

import com.cogent.cogentappointment.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cogent.cogentappointment.constants.SwaggerConstants.PatientConstant.BASE_PATIENT_API_VALUE;
import static com.cogent.cogentappointment.constants.SwaggerConstants.PatientConstant.SEARCH_PATIENT_WITH_SELF_OPERATION;
import static com.cogent.cogentappointment.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.constants.WebResourceKeyConstants.PatientConstant.BASE_PATIENT;
import static com.cogent.cogentappointment.constants.WebResourceKeyConstants.SEARCH;
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

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_PATIENT_WITH_SELF_OPERATION)
    public ResponseEntity<?> searchPatient(@RequestBody PatientSearchRequestDTO searchRequestDTO) {
        return ok(patientService.search(searchRequestDTO));
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
