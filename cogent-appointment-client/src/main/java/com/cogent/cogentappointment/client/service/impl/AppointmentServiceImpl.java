package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.*;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.repository.AppointmentTransactionInfoRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.client.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AppointmentServiceMessage.APPOINTMENT_EXISTS;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionDetailUtils.parseToAppointmentTransactionInfo;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final SpecializationService specializationService;

    private final AppointmentRepository appointmentRepository;

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final AppointmentTransactionInfoRepository appointmentTransactionInfoRepository;

    private final HospitalService hospitalService;

    public AppointmentServiceImpl(PatientService patientService,
                                  DoctorService doctorService,
                                  SpecializationService specializationService,
                                  AppointmentRepository appointmentRepository,
                                  DoctorDutyRosterRepository doctorDutyRosterRepository,
                                  DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                  AppointmentTransactionInfoRepository appointmentTransactionInfoRepository,
                                  HospitalService hospitalService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.appointmentRepository = appointmentRepository;
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentTransactionInfoRepository = appointmentTransactionInfoRepository;
        this.hospitalService = hospitalService;
    }

    @Override
    public AppointmentCheckAvailabilityMinResponseDTO checkAvailability(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        Date queryDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        AppointmentCheckAvailabilityMinResponseDTO responseDTO;

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES)) {
            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, new ArrayList<>());
        } else {
            final Duration rosterGapDuration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration())
                    .toStandardDuration();

            List<String> availableTimeSlots = filterDoctorTimeWithAppointments(
                    rosterGapDuration, doctorStartTime, doctorEndTime, requestDTO);

            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, availableTimeSlots);
        }

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentSuccessResponseDTO save(AppointmentRequestDTO appointmentRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                appointmentRequestDTO.getAppointmentDate(),
                appointmentRequestDTO.getAppointmentTime(),
                appointmentRequestDTO.getDoctorId(),
                appointmentRequestDTO.getSpecializationId());

        validateAppointmentExists(appointmentCount, appointmentRequestDTO.getAppointmentTime());

        Patient patient = fetchPatient(appointmentRequestDTO.getIsNewRegistration(),
                appointmentRequestDTO.getPatientId(),
                appointmentRequestDTO.getHospitalId(),
                appointmentRequestDTO.getPatientInfo()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentRequestDTO.getCreatedDateNepali());

        Appointment appointment = parseToAppointment(
                appointmentRequestDTO, appointmentNumber,
                patient,
                fetchSpecialization(appointmentRequestDTO.getSpecializationId()),
                fetchDoctor(appointmentRequestDTO.getDoctorId()),
                fetchHospital(appointmentRequestDTO.getHospitalId()));

        save(appointment);

        saveAppointmentTransactionDetail(appointmentRequestDTO.getTransactionInfo(), appointment);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentSuccessResponseDTO(appointmentNumber);
    }

    @Override
    public List<AppointmentPendingResponseDTO> fetchPendingAppointments(AppointmentPendingSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PENDING_APPOINTMENTS);

        List<AppointmentPendingResponseDTO> pendingAppointments =
                appointmentRepository.fetchPendingAppointments(searchDTO);

        log.info(FETCHING_PROCESS_STARTED, PENDING_APPOINTMENTS, getDifferenceBetweenTwoTime(startTime));

        return pendingAppointments;
    }


    /*WITH END TIME AND 12 HOUR FORMAT*/
    @Override
    public AppointmentCheckAvailabilityResponseDTO checkAvailabilityWithEndTime(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        AppointmentCheckAvailabilityResponseDTO responseDTO;

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES)) {
            responseDTO = parseToAppointmentResponseWithEndTime(doctorDutyRosterInfo, new ArrayList<>());
        } else {
            List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                    appointmentRepository.fetchBookedAppointments(requestDTO);

            List<AppointmentAvailabilityResponseDTO> availableSlots =
                    parseToResponseDTOWithEndTime(doctorDutyRosterInfo, bookedAppointments);

            responseDTO = parseToAppointmentResponseWithEndTime(doctorDutyRosterInfo, availableSlots);
        }

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                              Date toDate,
                                                                              Long doctorId,
                                                                              Long specializationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED);

        List<AppointmentBookedDateResponseDTO> bookedAppointmentDates =
                appointmentRepository.fetchBookedAppointmentDates(fromDate, toDate, doctorId, specializationId);

        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return bookedAppointmentDates;
    }

    @Override
    public Long fetchBookedAppointmentCount(Date fromDate, Date toDate,
                                            Long doctorId, Long specializationId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED);

        Long appointmentCount = appointmentRepository.fetchBookedAppointmentCount
                (fromDate, toDate, doctorId, specializationId);

        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return appointmentCount;
    }

    @Override
    public void update(AppointmentUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT);

        Appointment appointment = findById(updateRequestDTO.getAppointmentId());

//        Patient patient = fetchPatient(updateRequestDTO.getPatientId());

//        AdminAppointmentResponseDTO responseDTO = fetchAdminDetails(updateRequestDTO);
//
//        parseToUpdatedAppointment(appointment, updateRequestDTO, responseDTO, patient);

        //todo; appointment status in follow up tracker to be updated as per appointment

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void cancel(AppointmentCancelRequestDTO cancelRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CANCELLING_PROCESS_STARTED);

        Appointment appointment = findById(cancelRequestDTO.getId());

        convertToCancelledAppointment(appointment, cancelRequestDTO);

        log.info(CANCELLING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentMinimalResponseDTO> responseDTOS =
                appointmentRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);

        AppointmentDetailResponseDTO responseDTO = appointmentRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(RESCHEDULE_PROCESS_STARTED);

        Appointment appointment = findIncompleteAppointmentById(rescheduleRequestDTO.getAppointmentId());

//        Date originalAppointmentDate = appointment.getAppointmentDate();
//        Date originalAppointmentTime = appointment.getStartTime();

        parseToRescheduleAppointment(appointment, rescheduleRequestDTO);

        log.info(RESCHEDULE_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
    }

    //    @Override
//    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
//            AppointmentStatusRequestDTO requestDTO) {
//
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);
//
//        List<AppointmentStatusResponseDTO> responseDTOS =
//                appointmentRepository.fetchAppointmentForAppointmentStatus(requestDTO);
//
//        log.info(RESCHEDULE_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
//        return responseDTOS;
//    }
    private List<String> filterDoctorTimeWithAppointments(Duration rosterGapDuration,
                                                          String doctorStartTime,
                                                          String doctorEndTime,
                                                          AppointmentCheckAvailabilityRequestDTO requestDTO) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointments(requestDTO);

        return calculateAvailableTimeSlots(
                doctorStartTime, doctorEndTime, rosterGapDuration, bookedAppointments);
    }

    private void validateAppointmentExists(Long appointmentCount, String appointmentTime) {
        if (appointmentCount.intValue() > 0)
            throw new DataDuplicationException(String.format(APPOINTMENT_EXISTS,
                    convert24HourTo12HourFormat(appointmentTime)));
    }

    private Doctor fetchDoctor(Long doctorId) {
        return doctorService.fetchDoctorById(doctorId);
    }

    private Specialization fetchSpecialization(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }

    private Patient fetchPatient(Boolean isNewRegistration,
                                 Long patientId,
                                 Long hospitalId,
                                 PatientRequestDTO patientRequestDTO) {

        return isNewRegistration ? patientService.save(patientRequestDTO, hospitalId)
                : patientService.fetchPatient(patientId);
    }

    private Appointment findById(Long appointmentId) {
        return appointmentRepository.findAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Appointment findIncompleteAppointmentById(Long appointmentId) {
        return appointmentRepository.fetchIncompleteAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

//    private void saveFollowUpTracker(AppointmentRequestDTO appointmentRequestDTO,
//                                     String appointmentNumber,
//                                     AdminAppointmentResponseDTO responseDTO,
//                                     Patient patient) {
//
//        String parentAppointmentNumber;
//
//        if (Objects.isNull(appointmentRequestDTO.getParentAppointmentNumber())) {
//            parentAppointmentNumber = appointmentNumber;
//            followUpTrackerService.saveFollowUpTracker(
//                    appointmentRequestDTO.getAppointmentDate(),
//                    parentAppointmentNumber,
//                    responseDTO.getPatientType(),
//                    responseDTO.getDoctor(),
//                    patient);
//        } else {
//            parentAppointmentNumber = appointmentRequestDTO.getParentAppointmentNumber();
//            followUpTrackerService.updateNumberOfFollowupsInFollowUpTracker(
//                    parentAppointmentNumber,
//                    responseDTO.getDoctor().getId(),
//                    patient.getId());
//        }
//    }

    private DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterInfo(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId) {

        DoctorDutyRosterTimeResponseDTO overrideRosters =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideTime(date, doctorId, specializationId);

        if (Objects.isNull(overrideRosters))
            return doctorDutyRosterRepository.fetchDoctorDutyRosterTime(date, doctorId, specializationId);

        return overrideRosters;
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private void saveAppointmentTransactionDetail(AppointmentTransactionRequestDTO requestDTO,
                                                  Appointment appointment) {

        AppointmentTransactionDetail transactionDetail = parseToAppointmentTransactionInfo(requestDTO, appointment);
        appointmentTransactionInfoRepository.save(transactionDetail);
    }

}

