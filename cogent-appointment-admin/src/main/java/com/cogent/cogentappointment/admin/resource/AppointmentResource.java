package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentRequestDTO;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 2019-10-22
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentResource {

    private final AppointmentService appointmentService;

    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PutMapping(CHECK_AVAILABILITY)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> checkAvailability(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.checkAvailability(requestDTO));
    }

    @PostMapping
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.save(requestDTO));
    }

    @GetMapping(DETAILS + ID_PATH_VARIABLE_BASE)
    @ApiOperation(DETAILS_APPROVAL_VISIT_OPERATION)
    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id,
                                              @RequestParam("page") int page,
                                              @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok(appointmentService.fetchPendingApprovals(id,pageable));
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_OPERATION)
    public ResponseEntity<?> search(@RequestBody AppointmentPendingApprovalSearchDTO searchRequestDTO,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok().body(appointmentService.fetchPendingApprovals(searchRequestDTO, pageable));
    }

//    @PutMapping
//    @ApiOperation(UPDATE_OPERATION)
//    public ResponseEntity<?> update(@Valid @RequestBody AppointmentUpdateRequestDTO requestDTO) {
//        appointmentService.update(requestDTO);
//        return ok().build();
//    }
//
//    @DeleteMapping
//    @ApiOperation(DELETE_OPERATION)
//    public ResponseEntity<?> cancel(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
//        appointmentService.cancel(cancelRequestDTO);
//        return ok().build();
//    }
//
//    @PutMapping(SEARCH)
//    @ApiOperation(SEARCH_OPERATION)
//    public ResponseEntity<?> search(@RequestBody AppointmentSearchRequestDTO searchRequestDTO,
//                                    @RequestParam("page") int page,
//                                    @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ok().body(appointmentService.search(searchRequestDTO, pageable));
//    }
//
//    @GetMapping(DETAILS + ID_PATH_VARIABLE_BASE)
//    @ApiOperation(DETAILS_OPERATION)
//    public ResponseEntity<?> fetchDetailsById(@PathVariable("id") Long id) {
//        return ok(appointmentService.fetchDetailsById(id));
//    }
//
//    @PutMapping(RESCHEDULE)
//    @ApiOperation(RESCHEDULE_OPERATION)
//    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
//        appointmentService.rescheduleAppointment(requestDTO);
//        return ok().build();
//    }
}
