package com.cogent.cogentappointment.esewa.service.impl;


import com.cogent.cogentappointment.esewa.dto.request.appointment.*;
import com.cogent.cogentappointment.esewa.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestForDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.*;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.exception.DataDuplicationException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.log.constants.AppointmentReservationLog;
import com.cogent.cogentappointment.esewa.repository.*;
import com.cogent.cogentappointment.esewa.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.*;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_NOT_AVAILABLE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.*;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentReservationLog.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.esewa.utils.AppointmentFollowUpLogUtils.parseToAppointmentFollowUpLog;
import static com.cogent.cogentappointment.esewa.utils.AppointmentTransactionDetailUtils.parseToAppointmentTransactionInfo;
import static com.cogent.cogentappointment.esewa.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;

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

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentRescheduleLogRepository appointmentRescheduleLogRepository;

    private final AppointmentFollowUpLogRepository appointmentFollowUpLogRepository;

    private final AppointmentReservationLogRepository appointmentReservationLogRepository;

    private final HospitalPatientInfoService hospitalPatientInfoService;

    private final PatientMetaInfoService patientMetaInfoService;

    private final PatientRelationInfoService patientRelationInfoService;

    private final PatientRelationInfoRepository patientRelationInfoRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final  DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final HospitalRepository hospitalRepository;

    public AppointmentServiceImpl(PatientService patientService,
                                  DoctorService doctorService,
                                  SpecializationService specializationService,
                                  AppointmentRepository appointmentRepository,
                                  DoctorDutyRosterRepository doctorDutyRosterRepository,
                                  DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentRescheduleLogRepository appointmentRescheduleLogRepository,
                                  AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
                                  AppointmentReservationLogRepository appointmentReservationLogRepository,
                                  HospitalPatientInfoService hospitalPatientInfoService,
                                  PatientMetaInfoService patientMetaInfoService,
                                  PatientRelationInfoService patientRelationInfoService,
                                  PatientRelationInfoRepository patientRelationInfoRepository, HospitalRepository hospitalRepository) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.appointmentRepository = appointmentRepository;
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentRescheduleLogRepository = appointmentRescheduleLogRepository;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentReservationLogRepository = appointmentReservationLogRepository;
        this.hospitalPatientInfoService = hospitalPatientInfoService;
        this.patientMetaInfoService = patientMetaInfoService;
        this.patientRelationInfoService = patientRelationInfoService;
        this.patientRelationInfoRepository = patientRelationInfoRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public AppointmentCheckAvailabilityResponseDTO checkAvailability(
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        AppointmentCheckAvailabilityResponseDTO responseDTO =
                fetchAvailableTimeSlots(doctorDutyRosterInfo, requestDTO);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForSelf(AppointmentRequestDTOForSelf requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        validateRequestedAppointmentInfo(appointmentInfo);

        Hospital hospital = fetchHospital(appointmentInfo.getHospitalId());

        Patient patient = fetchPatientForSelf(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getPatientInfo()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentInfo.getHospitalId()
        );

        Appointment appointment = parseToAppointment(
                requestDTO.getAppointmentInfo(),
                appointmentNumber,
                YES,
                patient,
                fetchSpecialization(appointmentInfo.getSpecializationId(), appointmentInfo.getHospitalId()),
                fetchDoctor(appointmentInfo.getDoctorId(), appointmentInfo.getHospitalId()),
                hospital
        );

        save(appointment);

        saveAppointmentTransactionDetail(requestDTO.getTransactionInfo(), appointment);

        if (appointmentInfo.getIsFreeFollowUp().equals(YES))
            saveAppointmentFollowUpLog(appointmentInfo.getParentAppointmentId(), appointment.getId());

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentSuccessResponseDTO(appointmentNumber);
    }

    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForOthers(AppointmentRequestDTOForOthers requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        validateRequestedAppointmentInfo(appointmentInfo);

        Hospital hospital = fetchHospital(appointmentInfo.getHospitalId());

        Patient patient = fetchPatientForOthers(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getRequestBy(),
                requestDTO.getRequestFor()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentInfo.getHospitalId()
        );

        Appointment appointment = parseToAppointment(
                appointmentInfo,
                appointmentNumber,
                NO,
                patient,
                fetchSpecialization(appointmentInfo.getSpecializationId(), appointmentInfo.getHospitalId()),
                fetchDoctor(appointmentInfo.getDoctorId(), appointmentInfo.getHospitalId()),
                hospital
        );

        save(appointment);

        saveAppointmentTransactionDetail(requestDTO.getTransactionInfo(), appointment);

        if (appointmentInfo.getIsFreeFollowUp().equals(YES))
            saveAppointmentFollowUpLog(appointmentInfo.getParentAppointmentId(), appointment.getId());

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentSuccessResponseDTO(appointmentNumber);
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PENDING_APPOINTMENTS);

        List<AppointmentMinResponseDTO> pendingAppointments =
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

        log.info(RESCHEDULE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);

        AppointmentDetailResponseDTO appointmentDetails =
                appointmentRepository.fetchAppointmentDetails(appointmentId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return appointmentDetails;
    }

    @Override
    public List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentMinResponseDTO> appointmentHistory =
                appointmentRepository.fetchAppointmentHistory(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return appointmentHistory;
    }

    @Override
    public void cancelRegistration(Long appointmentReservationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_RESERVATION_LOG);

        com.cogent.cogentappointment.persistence.model.AppointmentReservationLog appointmentReservationLog =
                appointmentReservationLogRepository.findAppointmentReservationLogById(appointmentReservationId)
                        .orElseThrow(() -> new NoContentFoundException(AppointmentReservationLog.class));

        appointmentReservationLogRepository.delete(appointmentReservationLog);

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));
    }


    /*IF DOCTOR DAY OFF STATUS = 'Y', THEN THERE ARE NO AVAILABLE TIME SLOTS
    * ELSE, CALCULATE AVAILABLE TIME SLOTS BASED ON DOCTOR DUTY ROSTER AND APPOINTMENT FOR THE SELECTED CRITERIA
    * (APPOINTMENT DATE, DOCTOR AND SPECIALIZATION)
    * FETCH APPOINTMENT RESERVATIONS FOR THE SELECTED CRITERIA AND FINALLY FILTER OUT ONLY AVAILABLE TIME SLOTS
    * (EXCLUDING APPOINTMENT RESERVATION)*/
    private AppointmentCheckAvailabilityResponseDTO fetchAvailableTimeSlots(
            DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
            AppointmentCheckAvailabilityRequestDTO requestDTO) {

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

            List<String> bookedAppointmentReservations =
                    fetchBookedAppointmentReservations(requestDTO);

            filterAvailableSlotsWithAppointmentReservation(availableTimeSlots, bookedAppointmentReservations);

            responseDTO = parseToAvailabilityResponse(doctorStartTime, doctorEndTime, queryDate, availableTimeSlots);
        }
        return responseDTO;
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

    private List<String> fetchBookedAppointmentReservations
            (AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return appointmentReservationLogRepository.fetchBookedAppointmentReservations(requestDTO);
    }

    private void filterAvailableSlotsWithAppointmentReservation(List<String> availableTimeSlots,
                                                                List<String> appointmentReservations) {
        availableTimeSlots.removeAll(appointmentReservations);
    }

    private void validateAppointmentExists(Long appointmentCount, String appointmentTime) {
        if (appointmentCount.intValue() > 0)
            throw new DataDuplicationException(String.format(APPOINTMENT_EXISTS,
                    convert24HourTo12HourFormat(appointmentTime)));
    }

    private Doctor fetchDoctor(Long doctorId, Long hospitalId) {
        return doctorService.fetchActiveDoctorByIdAndHospitalId(doctorId, hospitalId);
    }

    private Specialization fetchSpecialization(Long specializationId, Long hospitalId) {
        return specializationService.fetchActiveSpecializationByIdAndHospitalId(specializationId, hospitalId);
    }

    private Patient fetchPatientForSelf(Boolean isNewRegistration,
                                        Long patientId,
                                        Hospital hospital,
                                        PatientRequestByDTO patientRequestDTO) {

        Patient patient;

        if (isNewRegistration) {
            patient = patientService.saveSelfPatient(patientRequestDTO);
            patientMetaInfoService.savePatientMetaInfo(patient);
        } else
            patient = patientService.fetchPatientById(patientId);

        hospitalPatientInfoService.saveHospitalPatientInfoForSelf(
                hospital, patient,
                patientRequestDTO.getEmail(),
                patientRequestDTO.getAddress()
        );

        return patient;
    }

    /*CASE 1: BOOK APPOINTMENT FOR SELF AND THEN FOR OTHERS
 * CASE 2 : BOOK APPOINTMENT FOR OTHERS AND THEN FOR SELF
 *
 * FIRST SAVE REQUESTED BY PATIENT INFO IF IT HAS NOT BEEN SAVED (CASE 2)
 * VALIDATE IF REQUESTED FOR PATIENT INFO EXISTS
 * IF YES,
 *      FETCH CORRESPONDING PATIENT RELATION INFO AND SIMPLY CHANGE STATUS AS ACTIVE IF IT IS DELETED
 *      OTHERWISE THROW PATIENT DUPLICITY EXCEPTION
 *      (THIS IS NECESSARY BECAUSE USER CAN DELETE 'OTHER' CARD
 *       AND ENTER SAME INFORMATION (NAME, MOBILE NUMBER, DATE OF BIRTH) AGAIN)
 * IF NO,
 *      SAVE CORRESPONDING REQUESTED CHILD PATIENT DETAILS IN
 *      'Patient', 'PatientMetaInfo' AND 'PatientRelationInfo' ENTITY
 *      SAVE IN PATIENT RELATION INFO (IF ALREADY SAVED- CHECK STATUS AND SET AS ACTIVE IF IT IS DELETED.)
 *
 * SAVE IN HOSPITAL PATIENT INFO IF IT HAS NOT BEEN SAVED PREVIOUSLY IN REQUESTED HOSPITAL
 * */
    private Patient fetchPatientForOthers(Boolean isNewRegistration,
                                          Long patientId,
                                          Hospital hospital,
                                          PatientRequestByDTO requestByPatientInfo,
                                          PatientRequestForDTO requestForPatientInfo) {

        Patient parentPatient;
        Patient childPatient;

        if (isNewRegistration) {
            parentPatient = patientService.saveSelfPatient(requestByPatientInfo);

            childPatient = patientService.fetchPatient(requestForPatientInfo);

            if (!Objects.isNull(childPatient)) {
                validatePatientDuplicity(parentPatient, childPatient, requestForPatientInfo);
            } else {
                childPatient = patientService.saveOtherPatient(requestForPatientInfo);
                patientMetaInfoService.savePatientMetaInfo(childPatient);
                patientRelationInfoService.savePatientRelationInfo(parentPatient, childPatient);
            }

        } else
            childPatient = patientService.fetchPatientById(patientId);

        hospitalPatientInfoService.saveHospitalPatientInfoForOthers(
                hospital, childPatient,
                requestForPatientInfo.getEmail(),
                requestForPatientInfo.getAddress()
        );

        return childPatient;
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

    /*FETCH DOCTOR DUTY ROSTER INFO FOR SELECTED DATE*/
    private DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterInfo(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId) {

        DoctorDutyRosterTimeResponseDTO overrideRosters =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideTime(date, doctorId, specializationId);

        if (Objects.isNull(overrideRosters))
            return doctorDutyRosterRepository.fetchDoctorDutyRosterTime(date, doctorId, specializationId);

        return overrideRosters;
    }

    private Hospital fetchHospital(Long hospitalId)
    {
        return fetchActiveHospital(hospitalId);
    }

    private void saveAppointmentTransactionDetail(AppointmentTransactionRequestDTO requestDTO,
                                                  Appointment appointment) {

        AppointmentTransactionDetail transactionDetail = parseToAppointmentTransactionInfo(requestDTO, appointment);
        appointmentTransactionDetailRepository.save(transactionDetail);
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

    private void saveAppointmentFollowUpLog(Long parentAppointmentId, Long followUpAppointmentId) {

        appointmentFollowUpLogRepository.save(
                parseToAppointmentFollowUpLog(parentAppointmentId, followUpAppointmentId)
        );
    }


    private void registerPatient(Long patientId, Long hospitalId) {
        patientService.registerPatient(patientId, hospitalId);
    }

    private void validatePatientDuplicity(Patient parentPatient,
                                          Patient childPatient,
                                          PatientRequestForDTO requestForPatientInfo) {

        PatientRelationInfo patientRelationInfo =
                patientRelationInfoRepository.fetchPatientRelationInfo(parentPatient.getId(), childPatient.getId());

        if (!Objects.isNull(patientRelationInfo)) {

            if (patientRelationInfo.getStatus().equals(DELETED))
                patientRelationInfo.setStatus(ACTIVE);
            else
                PATIENT_DUPLICATION_EXCEPTION(requestForPatientInfo.getName(),
                        requestForPatientInfo.getMobileNumber(), requestForPatientInfo.getDateOfBirth());
        }
    }

    private static void PATIENT_DUPLICATION_EXCEPTION(String name, String mobileNumber, Date dateOfBirth) {
        throw new DataDuplicationException(String.format(DUPLICATE_PATIENT_MESSAGE,
                name, mobileNumber, utilDateToSqlDate(dateOfBirth))
        );
    }

    private void validateRequestedAppointmentInfo(AppointmentRequestDTO appointmentInfo) {

        validateIfRequestIsBeforeCurrentDateTime(
                appointmentInfo.getAppointmentDate(), appointmentInfo.getAppointmentTime());

        validateIfParentAppointmentExists(appointmentInfo);

        validateIfAppointmentReservationExists(appointmentInfo);

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(appointmentInfo);

        validateIfRequestedAppointmentTimeIsValid(doctorDutyRosterInfo, appointmentInfo);

        boolean isTimeValid = validateIfRequestedAppointmentTimeIsValid(doctorDutyRosterInfo, appointmentInfo);

        if (!isTimeValid)
            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
                    convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime())));
    }

    /*VALIDATE IS REQUESTED DATE AND TIME IS AFTER CURRENT DATE AND TIME*/
    private void validateIfRequestIsBeforeCurrentDateTime(Date appointmentDate,
                                                          String appointmentTime) {

        Date requestDateTime = parseAppointmentTime(appointmentDate, appointmentTime);

        Date currentDateTime = new Date();

        boolean isRequestedBeforeCurrentDateTime = requestDateTime.before(currentDateTime);

        if (isRequestedBeforeCurrentDateTime)
            throw new BadRequestException(INVALID_APPOINTMENT_DATE_TIME);
    }

    /*VALIDATE IF APPOINTMENT EXISTS ON SELECTED DATE AND TIME */
    private void validateIfParentAppointmentExists(AppointmentRequestDTO appointmentInfo) {

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getAppointmentTime(),
                appointmentInfo.getDoctorId(),
                appointmentInfo.getSpecializationId()
        );

        validateAppointmentExists(appointmentCount, appointmentInfo.getAppointmentTime());
    }

    /*VALIDATE IF APPOINTMENT RESERVATION EXISTS ON SELECTED DATE AND TIME */
    private void validateIfAppointmentReservationExists(AppointmentRequestDTO appointmentInfo) {

        Long appointmentCount = appointmentReservationLogRepository.validateIfAppointmentReservationExists(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getAppointmentTime(),
                appointmentInfo.getDoctorId(),
                appointmentInfo.getSpecializationId()
        );

        validateAppointmentExists(appointmentCount, appointmentInfo.getAppointmentTime());
    }

    /*FETCH DOCTOR DUTY ROSTER FOR SELECTED DATE, DOCTOR AND SPECIALIZATION
    * IF DOCTOR DAY OFF = 'Y', THEN DOCTOR IS NOT AVAILABLE AND CANNOT TAKE AN APPOINTMENT*/
    private DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterInfo(AppointmentRequestDTO appointmentInfo) {

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getDoctorId(),
                appointmentInfo.getSpecializationId()
        );

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES))
            throw new NoContentFoundException(
                    String.format(DOCTOR_NOT_AVAILABLE, utilDateToSqlDate(appointmentInfo.getAppointmentDate())));

        return doctorDutyRosterInfo;
    }

    /*VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN DOCTOR DUTY ROSTER TIME SCHEDULES
    * IF IT MATCHES, THEN DO NOTHING
    * ELSE REQUESTED TIME IS INVALID AND THUS CANNOT TAKE AN APPOINTMENT*/
    private boolean validateIfRequestedAppointmentTimeIsValid(DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
                                                              AppointmentRequestDTO appointmentInfo) {

        final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(doctorStartTime));

        do {
            String date = FORMAT.print(startDateTime);

            final Duration rosterGapDuration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration())
                    .toStandardDuration();

            if (date.equals(appointmentInfo.getAppointmentTime()))
                return true;

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(doctorEndTime)) <= 0);

        return false;
    }

    public Hospital fetchActiveHospital(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = hospitalRepository.findActiveHospitalById(id)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return hospital;
    }

    private Function<Long, com.cogent.cogentappointment.client.exception.NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new com.cogent.cogentappointment.client.exception.NoContentFoundException(Hospital.class, "id", id.toString());
    };
}

