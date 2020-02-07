package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.PatientConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.PatientConstant.*;
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
    public ResponseEntity<?> search(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO) {
        return ok(patientService.search(searchRequestDTO));
    }

    @PutMapping(SEARCH + OTHERS)
    @ApiOperation(SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
    public ResponseEntity<?> search(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        return ok(patientService.fetchMinimalPatientInfo(searchRequestDTO, PageableUtils.getPageable(page, size)));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_BY_ID)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(patientService.fetchDetailsById(id));
    }

    @GetMapping(META_INFO + ACTIVE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_ACTIVE_PATIENT_META_INFO_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActivePatientMetaInfoForDropdown(@PathVariable("hospitalId") Long hospitalId) {
        return ok(patientService.patientMetaInfoActiveDropDownListByHospitalId(hospitalId));
    }

    @GetMapping(META_INFO + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_PATIENT_META_INFO_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchPatientMetaInfoForDropdown(@PathVariable("hospitalId") Long hospitalId) {
        return ok(patientService.patientMetaInfoDropDownListByHospitalId(hospitalId));
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
