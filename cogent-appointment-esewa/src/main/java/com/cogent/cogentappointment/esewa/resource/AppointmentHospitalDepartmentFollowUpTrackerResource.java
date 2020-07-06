package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentFollowUpTrackerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.FollowUpTrackerConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.FollowUpTrackerConstant.FETCH_FOLLOW_UP_DETAILS;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.FOLLOW_UP;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.utils.JWTDecryptUtils.toDecrypt;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 16/02/20
 */
@RequestMapping(API_V1 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT)
@RestController
@Api(BASE_API_VALUE)
public class AppointmentHospitalDepartmentFollowUpTrackerResource {

    private final AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDeptFollowUpTrackerService;

    public AppointmentHospitalDepartmentFollowUpTrackerResource(
            AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDeptFollowUpTrackerService) {
        this.appointmentHospitalDeptFollowUpTrackerService = appointmentHospitalDeptFollowUpTrackerService;
    }

    @PutMapping(FOLLOW_UP)
    @ApiOperation(FETCH_FOLLOW_UP_DETAILS)
    public ResponseEntity<?> fetchFollowUpTrackerDetails(@RequestBody Map<String, String> data) throws IOException {

        AppointmentHospitalDeptFollowUpRequestDTO requestDTO = convertValue(toDecrypt(data),
                AppointmentHospitalDeptFollowUpRequestDTO.class);

        return ok().body(appointmentHospitalDeptFollowUpTrackerService.fetchAppointmentHospitalDeptFollowUpDetails(requestDTO));
    }

}
