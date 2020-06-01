package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentHospitalDepartmentConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentHospitalDepartmentConstant.CHECK_APPOINTMENT_AVAILABILITY;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentHospitalDepartmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentHospitalDepartmentConstants.FETCH_AVAILABLE_TIMESLOTS;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
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

}
