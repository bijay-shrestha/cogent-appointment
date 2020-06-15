package com.cogent.cogentappointment.admin.resource;

import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentAppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.HospitalDepartmentTransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.HospitalDepartmentAppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.service.HospitalDepartmentLogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.HospitalDepartmentAppointmentLogConstant.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.HospitalDepartmentConstants.BASE_HOSPITAL_DEPARTMENT;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Sauravi Thapa ON 6/12/20
 */
@RequestMapping(API_V1)
@RestController
@Api(BASE_API_VALUE)
public class HospitalDepartmentLogsResource {

    private final HospitalDepartmentLogsService hospitalDepartmentLogsService;

    public HospitalDepartmentLogsResource(HospitalDepartmentLogsService hospitalDepartmentLogsService) {
        this.hospitalDepartmentLogsService = hospitalDepartmentLogsService;
    }

    @PutMapping(BASE_HOSPITAL_DEPARTMENT + BASE_APPOINTMENT + LOG)
    @ApiOperation(FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG)
    public ResponseEntity<?> fetchHospitalDepartmentAppointmentLog(
            @RequestBody HospitalDepartmentAppointmentLogSearchDTO searchRequestDTO,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalDepartmentLogsService.searchAppointmentLogs(searchRequestDTO, pageable));
    }

    @PutMapping(BASE_HOSPITAL_DEPARTMENT + TRANSACTION + LOG)
    @ApiOperation(FETCH_HOSPITAL_DEPARTMENT_TRANSACTION_LOG)
    public ResponseEntity<?> fetchHospitalDepartmentTransactionLog(
            @RequestBody HospitalDepartmentTransactionLogSearchDTO searchRequestDTO,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalDepartmentLogsService.searchTransactionLogs(searchRequestDTO, pageable));
    }

//    todo: test left
    @PutMapping(BASE_HOSPITAL_DEPARTMENT + RESCHEDULE + LOG)
    @ApiOperation(FETCH_HOSPITAL_DEPARTMENT_RESCHEDULE_LOG)
    public ResponseEntity<?> fetchHospitalDepartmentRescheduleLog(
            @RequestBody HospitalDepartmentAppointmentRescheduleLogSearchDTO searchRequestDTO,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ok().body(hospitalDepartmentLogsService.searchRescheduleLogs(searchRequestDTO, pageable));
    }


}
