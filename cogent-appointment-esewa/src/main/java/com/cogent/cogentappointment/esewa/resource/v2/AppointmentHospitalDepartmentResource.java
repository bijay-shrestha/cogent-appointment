package com.cogent.cogentappointment.esewa.resource.v2;

import com.cogent.cogentappointment.esewa.dto.request.DataWrapperRequest;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentConstant.CANCEL_REGISTRATION_OPERATION;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentHospitalDepartmentConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V2;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.CANCEL;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentHospitalDepartmentConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 28/05/20
 */
@RestController(API_V2 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT)
@RequestMapping(API_V2 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT)
@Api(BASE_API_VALUE)
public class AppointmentHospitalDepartmentResource {

    private final AppointmentHospitalDepartmentService appointmentHospitalDepartmentService;

    private final DataWrapperRequest dataWrapperRequest;

    public AppointmentHospitalDepartmentResource(AppointmentHospitalDepartmentService appointmentHospitalDepartmentService,
                                                 DataWrapperRequest dataWrapperRequest) {
        this.appointmentHospitalDepartmentService = appointmentHospitalDepartmentService;
        this.dataWrapperRequest = dataWrapperRequest;
    }

    @PutMapping(FETCH_AVAILABLE_TIMESLOTS)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> fetchAvailableTimeSlots() throws IOException {

        AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO = convertValue(dataWrapperRequest.getData(),
                AppointmentHospitalDeptCheckAvailabilityRequestDTO.class);

        return ok(appointmentHospitalDepartmentService.fetchAvailableTimeSlots(requestDTO));
    }

    @PutMapping(FETCH_AVAILABLE_TIMESLOTS + ROOM_WISE)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY_ROOM_WISE)
    public ResponseEntity<?> fetchAvailableTimeSlotsRoomWise() throws IOException {

        AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO requestDTO = convertValue(dataWrapperRequest.getData(),
                AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO.class);

        return ok(appointmentHospitalDepartmentService.fetchAvailableTimeSlotsRoomWise(requestDTO));
    }

    @GetMapping(CANCEL + APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(CANCEL_REGISTRATION_OPERATION)
    public ResponseEntity<?> cancelRegistration(@PathVariable("appointmentReservationId") Long appointmentReservationId) {
        return ok(appointmentHospitalDepartmentService.cancelRegistration(appointmentReservationId));
    }

    @GetMapping(AVAILABLE_DATES + HOSPITAL_DEPARTMENT_ID)
    @ApiOperation(FETCH_AVAILABLE_HOSPITAL_DEPARTMENT_DATES)
    public ResponseEntity<?> fetchAvailableAppointmentDate(@PathVariable("hospitalDepartmentId") Long hospitalDepartmentId) {
        return ok(appointmentHospitalDepartmentService.fetchAvailableDepartmentDates(hospitalDepartmentId));
    }
}
