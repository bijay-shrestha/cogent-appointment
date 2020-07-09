package com.cogent.cogentappointment.esewa.resource.v2;

import com.cogent.cogentappointment.esewa.dto.request.DataWrapperRequest;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentFollowUpTrackerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.FollowUpTrackerConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.FollowUpTrackerConstant.FETCH_FOLLOW_UP_DETAILS;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.API_V2;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.BASE_APPOINTMENT;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.FOLLOW_UP;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.utils.commons.ObjectMapperUtils.convertValue;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti on 16/02/20
 */
@RestController(API_V2 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT + FOLLOW_UP)
@RequestMapping(API_V2 + BASE_APPOINTMENT + BASE_HOSPITAL_DEPARTMENT + FOLLOW_UP)
@Api(BASE_API_VALUE)
public class AppointmentHospitalDepartmentFollowUpTrackerResource {

    private final AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDeptFollowUpTrackerService;

    private final DataWrapperRequest dataWrapperRequest;

    public AppointmentHospitalDepartmentFollowUpTrackerResource(
            AppointmentHospitalDepartmentFollowUpTrackerService appointmentHospitalDeptFollowUpTrackerService,
            DataWrapperRequest dataWrapperRequest) {
        this.appointmentHospitalDeptFollowUpTrackerService = appointmentHospitalDeptFollowUpTrackerService;
        this.dataWrapperRequest = dataWrapperRequest;
    }

    @PutMapping
    @ApiOperation(FETCH_FOLLOW_UP_DETAILS)
    public ResponseEntity<?> fetchFollowUpTrackerDetails() throws IOException {

        AppointmentHospitalDeptFollowUpRequestDTO requestDTO = convertValue(dataWrapperRequest.getData(),
                AppointmentHospitalDeptFollowUpRequestDTO.class);

        return ok().body(appointmentHospitalDeptFollowUpTrackerService.fetchAppointmentHospitalDeptFollowUpDetails(requestDTO));
    }

}
