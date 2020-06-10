package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.*;
import com.cogent.cogentappointment.admin.service.AppointmentTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AppointmentTransferConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentTransferConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 5/6/20
 */

@RestController
@RequestMapping(API_V1 + BASE_APPOINTMENT_TRANSFER)
@Api(BASE_API_VALUE)
public class AppointmentTransferResource {

    private final AppointmentTransferService appointmentTransferService;

    public AppointmentTransferResource(AppointmentTransferService appointmentTransferService) {
        this.appointmentTransferService = appointmentTransferService;
    }

    @PutMapping(APPOINTMENT_DATE)
    @ApiOperation(FETCH_AVAILABLE_DATES)
    public ResponseEntity<?> fetchDoctorAvailableDates(@Valid @RequestBody AppointmentDateRequestDTO requestDTO) {
        return ok(appointmentTransferService.fetchAvailableDatesByDoctorIdAndSpecializationId(requestDTO));
    }

    @PutMapping(APPOINTMENT_TIME)
    @ApiOperation(FETCH_AVAILABLE_TIME)
    public ResponseEntity<?> fetchDoctorAvailableTime(@Valid @RequestBody AppointmentTransferTimeRequestDTO requestDTO) {
        return ok(appointmentTransferService.fetchAvailableDoctorTime(requestDTO));
    }

    @PutMapping(APPOINTMENT_CHARGE)
    @ApiOperation(FETCH_DOCTOR_CHARGE)
    public ResponseEntity<?> fetchDoctorChargeByDoctorId(@Valid @RequestBody DoctorChargeRequestDTO requestDTO) {
        return ok(appointmentTransferService.fetchDoctorChargeByDoctorId(requestDTO));
    }

    @PutMapping
    @ApiOperation(APPOINTMENT_TRANSFER)
    public ResponseEntity<?> appointmentTransfer(@Valid @RequestBody AppointmentTransferRequestDTO requestDTO) {
        appointmentTransferService.appointmentTransfer(requestDTO);
        return ok().build();
    }

    @PutMapping(SEARCH)
    @ApiOperation(FETCH_TRANSFERRED_APPOINTMENT_LIST)
    public ResponseEntity<?> searchAppointmentTransfer(@Valid @RequestBody AppointmentTransferSearchRequestDTO requestDTO,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ok(appointmentTransferService.searchTransferredAppointment(requestDTO,pageable));
    }

    @GetMapping(DETAIL + APPOINTMENT_TRANSFER_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_TRANSFERRED_APPOINTMENT_DETAIL)
    public ResponseEntity<?> fetchAppointmentTransferDetailById(@PathVariable("appointmentTransferId") Long appointmentTransferId) {
        return ok(appointmentTransferService.fetchAppointmentTransferDetailById(appointmentTransferId));
    }
}
