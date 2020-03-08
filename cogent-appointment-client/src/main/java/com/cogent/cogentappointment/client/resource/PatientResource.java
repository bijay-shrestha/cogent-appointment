package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.patient.*;
import com.cogent.cogentappointment.client.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.PatientConstant.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.AppointmentConstants.APPOINTMENT_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.PatientConstant.BASE_PATIENT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.PatientConstant.HOSPITAL_PATIENT_INFO_ID_PATH_VARIABLE_BASE;
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
    public ResponseEntity<?> searchForSelf(@Valid @RequestBody PatientMinSearchRequestDTO searchRequestDTO) {
        return ok(patientService.searchForSelf(searchRequestDTO));
    }

    @PutMapping(SEARCH + OTHERS)
    @ApiOperation(SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
    public ResponseEntity<?> search(@Valid @RequestBody PatientMinSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        return ok(patientService.searchForOthers(searchRequestDTO, getPageable(page, size)));
    }

    @GetMapping(DETAIL + OTHERS + HOSPITAL_PATIENT_INFO_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_OF_OTHERS)
    public ResponseEntity<?> fetchMinPatientDetailsOfOthers(@PathVariable("hospitalPatientInfoId")
                                                                    Long hospitalPatientInfoId) {
        return ok(patientService.fetchMinPatientDetailsOfOthers(hospitalPatientInfoId));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_BY_ID)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(patientService.fetchDetailsById(id));
    }

    @PutMapping(UPDATE + OTHERS)
    @ApiOperation(UPDATE_PATIENT_INFO_OPERATION)
    public ResponseEntity<?> updateOtherPatientDetails(@Valid @RequestBody PatientUpdateDTOForOthers updateRequestDTO) {
        patientService.updateOtherPatientDetails(updateRequestDTO);
        return ok().build();
    }

    @PutMapping(DELETE + OTHERS)
    @ApiOperation(DELETE_PATIENT_INFO_OPERATION)
    public ResponseEntity<?> deleteOtherPatientDetails(@Valid @RequestBody
                                                               PatientDeleteRequestDTOForOthers requestDTOForOthers) {
        patientService.deleteOtherPatient(requestDTOForOthers);
        return ok().build();
    }

    /*admin*/
    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> searchPatient(@Valid @RequestBody PatientSearchRequestDTO searchRequestDTO,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size) {
        return ok(patientService.search(searchRequestDTO, getPageable(page, size)));
    }

    @PutMapping
    @ApiOperation(UPDATE_PATIENT_INFO_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody PatientUpdateRequestDTO updateRequestDTO) {
        patientService.update(updateRequestDTO);
        return ok().build();
    }

    @GetMapping(META_INFO + ACTIVE + MIN)
    @ApiOperation(FETCH_ACTIVE_MIN_PATIENT_META_INFO)
    public ResponseEntity<?> fetchActiveMinPatientMetaInfo() {
        return ok(patientService.fetchActiveMinPatientMetaInfo());
    }

    @GetMapping(META_INFO + MIN)
    @ApiOperation(FETCH_MIN_PATIENT_META_INFO)
    public ResponseEntity<?> fetchPatientMetaInfoForDropdown() {
        return ok(patientService.fetchMinPatientMetaInfo());
    }

    @GetMapping(MIN + DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_PATIENT_MIN_DETAIL_BY_APPOINTMENT_ID)
    public ResponseEntity<?> fetchDetailByAppointmentId(@PathVariable("appointmentId") Long appointmentId) {
        return ok(patientService.fetchDetailByAppointmentId(appointmentId));
    }
}
