package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.DataWrapperRequest;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientDeleteRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientUpdateDTOForOthers;
import com.cogent.cogentappointment.esewa.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.PatientConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.PatientConstant.BASE_PATIENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.PatientConstant.HOSPITAL_PATIENT_INFO_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.utils.JWTDecryptUtils.toDecrypt;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;
import static com.cogent.cogentappointment.esewa.utils.commons.PageableUtils.getPageable;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RestController
@RequestMapping(API_V1 + BASE_PATIENT)
@Api(value = BASE_PATIENT_API_VALUE)
public class PatientResource {

    private final PatientService patientService;

    private final DataWrapperRequest dataWrapperRequest;

    public PatientResource(PatientService patientService, DataWrapperRequest dataWrapperRequest) {
        this.patientService = patientService;
        this.dataWrapperRequest = dataWrapperRequest;
    }

    //    /*WITHOUT HOSPITAL WISE*/
//    @PutMapping(SEARCH + SELF)
//    @ApiOperation(SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION)
//    public ResponseEntity<?> searchForSelf(@Valid @RequestBody PatientMinSearchRequestDTO searchRequestDTO) {
//        return ok(patientService.searchForSelf(searchRequestDTO));
//    }

    /*HOSPITAL WISE*/
    @PutMapping(SEARCH + SELF)
    @ApiOperation(SEARCH_PATIENT_WITH_SELF_TYPE_OPERATION)
    public ResponseEntity<?> searchForSelf() throws IOException {

        PatientMinSearchRequestDTO searchRequestDTO = convertValue(dataWrapperRequest.getData(),
                PatientMinSearchRequestDTO.class);

        return ok(patientService.searchForSelfHospitalWise(searchRequestDTO));
    }

//    @PutMapping(SEARCH + OTHERS)
//    @ApiOperation(SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
//    public ResponseEntity<?> search(@Valid @RequestBody PatientMinSearchRequestDTO searchRequestDTO,
//                                    @RequestParam("page") int page,
//                                    @RequestParam("size") int size) {
//        return ok(patientService.searchForOthers(searchRequestDTO, getPageable(page, size)));
//    }

    /*HOSPITAL WISE*/
    @PutMapping(SEARCH + OTHERS)
    @ApiOperation(SEARCH_PATIENT_WITH_OTHERS_TYPE_OPERATION)
    public ResponseEntity<?> search(@RequestParam("page") int page,
                                    @RequestParam("size") int size) throws IOException {

        PatientMinSearchRequestDTO searchRequestDTO = convertValue(dataWrapperRequest.getData(),
                PatientMinSearchRequestDTO.class);

        return ok(patientService.searchForOthersHospitalWise(searchRequestDTO, getPageable(page, size)));
    }

    @GetMapping(DETAIL + OTHERS + HOSPITAL_PATIENT_INFO_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_OF_OTHERS)
    public ResponseEntity<?> fetchMinPatientDetailsOfOthers(@PathVariable("hospitalPatientInfoId")
                                                                    Long hospitalPatientInfoId) {
        return ok(patientService.fetchMinPatientDetailsOfOthers(hospitalPatientInfoId));
    }

    @PutMapping(UPDATE + OTHERS)
    @ApiOperation(UPDATE_PATIENT_INFO_OPERATION)
    public ResponseEntity<?> updateOtherPatientDetails() throws IOException {


        PatientUpdateDTOForOthers updateRequestDTO = convertValue(dataWrapperRequest.getData(),
                PatientUpdateDTOForOthers.class);

        patientService.updateOtherPatientDetails(updateRequestDTO);
        return ok().build();
    }

    @PutMapping(DELETE + OTHERS)
    @ApiOperation(DELETE_PATIENT_INFO_OPERATION)
    public ResponseEntity<?> deleteOtherPatientDetails() throws IOException {

        PatientDeleteRequestDTOForOthers requestDTOForOthers = convertValue(dataWrapperRequest.getData(),
                PatientDeleteRequestDTOForOthers.class);

        patientService.deleteOtherPatient(requestDTOForOthers);
        return ok().build();
    }
}
