package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.*;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentPendingResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.Minutes;
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

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    private final AppointmentRescheduleLogRepository appointmentRescheduleLogRepository;

    public AppointmentServiceImpl(PatientService patientService,
                                  DoctorService doctorService,
                                  SpecializationService specializationService,
                                  AppointmentRepository appointmentRepository,
                                  DoctorDutyRosterRepository doctorDutyRosterRepository,
                                  DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                  AppointmentTransactionInfoRepository appointmentTransactionInfoRepository,
                                  HospitalService hospitalService,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  PatientMetaInfoRepository patientMetaInfoRepository,
                                  AppointmentRescheduleLogRepository appointmentRescheduleLogRepository) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.appointmentRepository = appointmentRepository;
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentTransactionInfoRepository = appointmentTransactionInfoRepository;
        this.hospitalService = hospitalService;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.patientMetaInfoRepository = patientMetaInfoRepository;
        this.appointmentRescheduleLogRepository = appointmentRescheduleLogRepository;
    }

    @Override
    public AppointmentCheckAvailabilityResponseDTO checkAvailability(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        Date queryDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        AppointmentCheckAvailabilityResponseDTO responseDTO;

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

        log.info(FETCHING_PROCESS_COMPLETED, PENDING_APPOINTMENTS, getDifferenceBetweenTwoTime(startTime));

        return pendingAppointments;
    }

    @Override
    public void cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CANCELLING_PROCESS_STARTED);

        Appointment appointment = findPendingAppointmentById(cancelRequestDTO.getAppointmentId());

        parseAppointmentCancelledDetails(appointment, cancelRequestDTO.getRemarks());

        Double refundAmount = appointmentRepository.calculateRefundAmount(cancelRequestDTO.getAppointmentId());

        saveAppointmentRefundDetail(
                parseToAppointmentRefundDetail(appointment, refundAmount)
        );

        log.info(CANCELLING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(RESCHEDULE_PROCESS_STARTED);

        Appointment appointment = findPendingAppointmentById(rescheduleRequestDTO.getAppointmentId());

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime(),
                appointment.getDoctorId().getId(),
                appointment.getSpecializationId().getId());

        validateAppointmentExists(appointmentCount, rescheduleRequestDTO.getRescheduleTime());

        saveAppointmentRescheduleLog(appointment, rescheduleRequestDTO);

        parseToRescheduleAppointment(appointment, rescheduleRequestDTO);

        log.info(RESCHEDULE_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
    }

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
                : patientService.fetchActivePatientById(patientId);
    }

    private Appointment findPendingAppointmentById(Long appointmentId) {
        return appointmentRepository.fetchPendingAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

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

    private void saveAppointmentRefundDetail(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private void saveAppointmentRescheduleLog(Appointment appointment,
                                              AppointmentRescheduleRequestDTO rescheduleRequestDTO) {
        AppointmentRescheduleLog appointmentRescheduleLog = parseToAppointmentRescheduleLog(
                appointment, rescheduleRequestDTO);
        appointmentRescheduleLogRepository.save(appointmentRescheduleLog);
    }

}

