package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RestController
@RequestMapping(WebResourceKeyConstants.API_V1 + WebResourceKeyConstants.PatientConstant.BASE_PATIENT)
@Api(value = SwaggerConstants.PatientConstant.BASE_PATIENT_API_VALUE)
public class PatientResource {

    private final PatientService patientService;

    public PatientResource(PatientService patientService) {
        this.patientService = patientService;
    }

    @PutMapping(WebResourceKeyConstants.SEARCH + WebResourceKeyConstants.PatientConstant.SELF)
    @ApiOperation(SwaggerConstants.PatientConstant.SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION)
    public ResponseEntity<?> search(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO) {
        return ok(patientService.search(searchRequestDTO));
    }

    @PutMapping(WebResourceKeyConstants.SEARCH + WebResourceKeyConstants.PatientConstant.OTHERS)
    @ApiOperation(SwaggerConstants.PatientConstant.SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
    public ResponseEntity<?> search(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        return ok(patientService.fetchMinimalPatientInfo(searchRequestDTO, PageableUtils.getPageable(page, size)));
    }

    @GetMapping(WebResourceKeyConstants.DETAIL + WebResourceKeyConstants.ID_PATH_VARIABLE_BASE)
    @ApiOperation(SwaggerConstants.PatientConstant.FETCH_DETAILS_BY_ID)
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
