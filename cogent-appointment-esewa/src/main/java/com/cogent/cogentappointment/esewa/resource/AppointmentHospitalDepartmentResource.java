package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentConstant.CANCEL_REGISTRATION_OPERATION;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentHospitalDepartmentConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.CANCEL;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentHospitalDepartmentConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 28/05/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentHospitalDepartmentResource {

    private final AppointmentHospitalDepartmentService appointmentHospitalDepartmentService;

    public AppointmentHospitalDepartmentResource(AppointmentHospitalDepartmentService appointmentHospitalDepartmentService) {
        this.appointmentHospitalDepartmentService = appointmentHospitalDepartmentService;
    }

    @PutMapping(FETCH_AVAILABLE_TIMESLOTS)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> fetchAvailableTimeSlots(@Valid @RequestBody
                                                             AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentHospitalDepartmentService.fetchAvailableTimeSlots(requestDTO));
    }

    @PutMapping(FETCH_AVAILABLE_TIMESLOTS + ROOM_WISE)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY_ROOM_WISE)
    public ResponseEntity<?> fetchAvailableTimeSlotsRoomWise(@Valid @RequestBody
                                                                     AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO requestDTO) {
        return ok(appointmentHospitalDepartmentService.fetchAvailableTimeSlotsRoomWise(requestDTO));
    }

    @GetMapping(CANCEL + APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(CANCEL_REGISTRATION_OPERATION)
    public ResponseEntity<?> cancelRegistration(@PathVariable("appointmentReservationId") Long appointmentReservationId) {
        return ok(appointmentHospitalDepartmentService.cancelRegistration(appointmentReservationId));
    }

    @GetMapping(AVAILABLE_DATES+ HOSPITAL_DEPARTMENT_ID)
    @ApiOperation(FETCH_AVAILABLE_HOSPITAL_DEPARTMENT_DATES)
    public ResponseEntity<?> fetchAvailableAppointmentDate(@PathVariable("hospitalDepartmentId") Long hospitalDepartmentId){
        return ok(appointmentHospitalDepartmentService.fetchAvailableDepartmentDates(hospitalDepartmentId));
    }
}
