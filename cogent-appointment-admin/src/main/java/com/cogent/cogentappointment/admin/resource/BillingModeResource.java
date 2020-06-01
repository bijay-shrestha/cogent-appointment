package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.service.BillingModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.BillingModeConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentModeConstants.BASE_APPOINTMENT_MODE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.BillingModeConstants.BASE_Billing_MODE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalConstants.HOSPITAL_WISE;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
@RestController
@RequestMapping(value = API_V1 + BASE_Billing_MODE)
@Api(BASE_API_VALUE)
public class BillingModeResource {

    private final BillingModeService service;

    public BillingModeResource(BillingModeService service) {
        this.service = service;
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody BillingModeRequestDTO requestDTO) {
        service.save(requestDTO);
        return created(create(API_V1 + BASE_APPOINTMENT_MODE)).build();
    }

    @PutMapping
    @ApiOperation(UPDATE_OPERATION)
    public ResponseEntity<?> update(@Valid @RequestBody BillingModeUpdateRequestDTO updateRequestDTO) {
        service.update(updateRequestDTO);
        return ok().build();
    }

    @DeleteMapping
    @ApiOperation(DELETE_OPERATION)
    public ResponseEntity<?> delete(@Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {
        service.delete(deleteRequestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody BillingModeSearchRequestDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(service.search(searchRequestDTO, pageable));
    }

    @GetMapping(DETAIL + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
        return ok(service.fetchDetailsById(id));
    }

    @GetMapping(ACTIVE + MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveMinBillingMode() {
        return ok(service.fetchActiveMinBillingMode());
    }

    @GetMapping(HOSPITAL_WISE + ACTIVE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchActiveMinBillingModeByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(service.fetchActiveMinBillingModeByHospitalId(hospitalId));
    }

    @GetMapping(MIN)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchMinBillingMode() {
        return ok(service.fetchMinBillingMode());
    }

    @GetMapping(HOSPITAL_WISE + MIN + HOSPITAL_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_DETAILS_FOR_DROPDOWN)
    public ResponseEntity<?> fetchMinBillingModeByHospitalId(@PathVariable("hospitalId") Long hospitalId) {
        return ok(service.fetchMinBillingModeByHospitalId(hospitalId));
    }
}
