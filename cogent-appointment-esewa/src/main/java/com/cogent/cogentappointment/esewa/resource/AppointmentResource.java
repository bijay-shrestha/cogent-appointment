package com.cogent.cogentappointment.esewa.resource;

import com.cogent.cogentappointment.esewa.dto.request.appointment.appointmentTxnStatus.AppointmentTransactionStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AvailableDoctorRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.esewa.service.AppointmentService;
import com.cogent.cogentappointment.esewa.service.EsewaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AppointmentConstant.*;
import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.EsewaConstant.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.AppointmentConstants.*;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.EsewaConstants.*;
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

    private final EsewaService esewaService;

    public AppointmentResource(AppointmentService appointmentService,
                               EsewaService esewaService) {
        this.appointmentService = appointmentService;
        this.esewaService = esewaService;
    }

    @PutMapping(FETCH_AVAILABLE_TIMESLOTS)
    @ApiOperation(CHECK_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> fetchAvailableTimeSlots(@Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.fetchAvailableTimeSlots(requestDTO));
    }

    @PutMapping(FETCH_CURRENT_AVAILABLE_TIMESLOTS)
    @ApiOperation(CHECK_CURRENT_APPOINTMENT_AVAILABILITY)
    public ResponseEntity<?> fetchCurrentAvailableTimeSlots(
            @Valid @RequestBody AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return ok(appointmentService.fetchCurrentAvailableTimeSlots(requestDTO));
    }

    @PostMapping(SELF)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> saveAppointmentForSelf(@Valid @RequestBody AppointmentRequestDTOForSelf requestDTO) {

        System.out.println("AppointmentRequestDTO----->  ");
        System.out.println("isNewRegistration ------->" + requestDTO.getAppointmentInfo().getIsNewRegistration());
        System.out.println("patientId ------->" + requestDTO.getAppointmentInfo().getPatientId());
        System.out.println("createdDateNepali ------->" + requestDTO.getAppointmentInfo().getCreatedDateNepali());
        System.out.println("isFollowUp ------->" + requestDTO.getAppointmentInfo().getIsFollowUp());
        System.out.println("parentAppointmentId ------->" + requestDTO.getAppointmentInfo().getParentAppointmentId());
        System.out.println("appointmentReservationId ------->" + requestDTO.getAppointmentInfo().getAppointmentReservationId());
        System.out.println("hospitalAppointmentServiceTypeId ------->" + requestDTO.getAppointmentInfo().getHospitalAppointmentServiceTypeId());

        System.out.println("PatientRequestByDTO----->  ");
        System.out.println("name ------->" + requestDTO.getPatientInfo().getName());
        System.out.println("mobileNumber ------->" + requestDTO.getPatientInfo().getMobileNumber());
        System.out.println("gender ------->" + requestDTO.getPatientInfo().getGender());
        System.out.println("dateOfBirth ------->" + requestDTO.getPatientInfo().getDateOfBirth());
        System.out.println("email ------->" + requestDTO.getPatientInfo().getEmail());
        System.out.println("eSewaId ------->" + requestDTO.getPatientInfo().getESewaId());
        System.out.println("address ------->" + requestDTO.getPatientInfo().getAddress());
        System.out.println("isAgent ------->" + requestDTO.getPatientInfo().getIsAgent());
        System.out.println("provinceId ------->" + requestDTO.getPatientInfo().getProvinceId());
        System.out.println("vdcOrMunicipalityId ------->" + requestDTO.getPatientInfo().getVdcOrMunicipalityId());
        System.out.println("districtId ------->" + requestDTO.getPatientInfo().getDistrictId());
        System.out.println("wardNumber ------->" + requestDTO.getPatientInfo().getWardNumber());

        System.out.println("AppointmentTransactionRequestDTO----->  ");
        System.out.println("transactionDate ------->" + requestDTO.getTransactionInfo().getTransactionDate());
        System.out.println("transactionNumber ------->" + requestDTO.getTransactionInfo().getTransactionNumber());
        System.out.println("appointmentModeCode ------->" + requestDTO.getTransactionInfo().getAppointmentModeCode());


        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.saveAppointmentForSelf(requestDTO));
    }

    @PostMapping(OTHERS)
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> saveAppointmentForOthers(@Valid @RequestBody AppointmentRequestDTOForOthers requestDTO) {
        return created(create(API_V1 + BASE_APPOINTMENT)).body(appointmentService.saveAppointmentForOthers(requestDTO));
    }

    @PutMapping(PENDING_APPOINTMENT)
    @ApiOperation((FETCH_PENDING_APPOINTMENT))
    public ResponseEntity<?> fetchPendingAppointments(@RequestBody AppointmentHistorySearchDTO searchDTO) {
        return ok(appointmentService.fetchPendingAppointments(searchDTO));
    }

    @PutMapping(CANCEL)
    @ApiOperation(CANCEL_APPOINTMENT_OPERATION)
    public ResponseEntity<?> cancelAppointment(@Valid @RequestBody AppointmentCancelRequestDTO cancelRequestDTO) {
        return ok(appointmentService.cancelAppointment(cancelRequestDTO));
    }

    @PutMapping(RESCHEDULE)
    @ApiOperation(RESCHEDULE_OPERATION)
    public ResponseEntity<?> rescheduleAppointment(@Valid @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
        return ok(appointmentService.rescheduleAppointment(requestDTO));
    }

    @GetMapping(DETAIL + APPOINTMENT_ID_PATH_VARIABLE_BASE)
    public ResponseEntity<?> fetchAppointmentDetails(@PathVariable("appointmentId") Long appointmentId) {
        return ok().body(appointmentService.fetchAppointmentDetails(appointmentId));
    }

    @PutMapping(HISTORY)
    @ApiOperation(FETCH_APPOINTMENT_HISTORY)
    public ResponseEntity<?> fetchAppointmentHistory(@RequestBody AppointmentHistorySearchDTO searchDTO) {
        return ok(appointmentService.fetchAppointmentHistory(searchDTO));
    }

    @PutMapping(SEARCH)
    @ApiOperation(SEARCH_APPOINTMENT)
    public ResponseEntity<?> searchAppointments(@RequestBody AppointmentSearchDTO searchDTO) {
        return ok(appointmentService.searchAppointments(searchDTO));
    }

    @GetMapping(CANCEL + APPOINTMENT_RESERVATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(CANCEL_REGISTRATION_OPERATION)
    public ResponseEntity<?> cancelRegistration(@PathVariable("appointmentReservationId") Long appointmentReservationId) {
        return ok(appointmentService.cancelRegistration(appointmentReservationId));
    }

    @PutMapping(TRANSACTION_STATUS)
    @ApiOperation(FETCH_APPOINTMENT_TRANSACTION_STATUS)
    public ResponseEntity<?> fetchAppointmentTransactionStatus(
            @Valid @RequestBody AppointmentTransactionStatusRequestDTO requestDTO) {
        return ok(appointmentService.fetchAppointmentTransactionStatus(requestDTO));
    }

    @PutMapping(AVAILABLE_APPOINTMENT_DATES_AND_TIME)
    @ApiOperation(FETCH_AVAILABLE_APPOINTMENT_DATES)
    public ResponseEntity<?> fetchAvailableDatesAndTime(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDatesAndTime(requestDTO));
    }

    @PutMapping(FETCH_DOCTOR_AVAILABLE_STATUS)
    @ApiOperation(FETCH_DOCTOR_AVAILABLE_STATUS_OPERATION)
    public ResponseEntity<?> fetchDoctorAvailableStatus(@RequestBody AppointmentDetailRequestDTO requestDTO) {
        return ok(esewaService.fetchDoctorAvailableStatus(requestDTO));
    }

    @GetMapping(DOCTOR_AVAILABLE_DATES + DOCTOR_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_DOCTOR_DATES)
    public ResponseEntity<?> fetchAvailableDatesWithSpecialization(@PathVariable("doctorId") Long doctorId) {
        return ok(esewaService.fetchAvailableDatesWithSpecialization(doctorId));
    }

    @GetMapping(SPECIALIZATION_AVAILABLE_DATES + SPECIALIZATION_ID_PATH_VARIABLE_BASE)
    @ApiOperation(FETCH_AVAILABLE_SPECIALIZATION_DATES)
    public ResponseEntity<?> fetchAvailableDatesWithDoctor(@PathVariable("specializationId") Long specializationId) {
        return ok(esewaService.fetchAvailableDatesWithDoctor(specializationId));
    }

    @PutMapping(DOCTOR_WITH_SPECIALIZATION_AVAILABLE_DATES)
    @ApiOperation(FETCH_AVAILABLE_DATES)
    public ResponseEntity<?> fetchAvailableDates(@RequestBody AppointmentDatesRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDates(requestDTO));
    }

//    /*difference is to fetch available doctors within a choosen date VS multiple from date/to date*/
//    @PutMapping(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION)
//    @ApiOperation(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION)
//    public ResponseEntity<?> fetchAvailableDoctorWithSpecialization(@RequestBody AppointmentDetailRequestDTO requestDTO) {
//        return ok(esewaService.fetchAvailableDoctorWithSpecialization(requestDTO));
//    }

    @PutMapping(FETCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION)
    @ApiOperation(SEARCH_AVAILABLE_DOCTORS_WITH_SPECIALIZATION_OPERATION)
    public ResponseEntity<?> fetchAvailableDoctorWithSpecialization(@RequestBody AvailableDoctorRequestDTO requestDTO) {
        return ok(esewaService.fetchAvailableDoctorWithSpecialization(requestDTO));
    }


}
