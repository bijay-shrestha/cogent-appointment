package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestForDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AppointmentServiceMessage.*;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_NOT_AVAILABLE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.REFUNDED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentReservationLogConstant.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpLogUtils.parseToAppointmentFollowUpLog;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionDetailUtils.parseToAppointmentTransactionInfo;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionRequestLogUtils.parseToAppointmentTransactionStatusResponseDTO;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionRequestLogUtils.updateAppointmentTransactionRequestLog;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

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

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final HospitalService hospitalService;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentRescheduleLogRepository appointmentRescheduleLogRepository;

    private final AppointmentFollowUpLogRepository appointmentFollowUpLogRepository;

    private final AppointmentReservationLogRepository appointmentReservationLogRepository;

    private final AppointmentFollowUpTrackerService appointmentFollowUpTrackerService;

    private final HospitalPatientInfoService hospitalPatientInfoService;

    private final PatientMetaInfoService patientMetaInfoService;

    private final PatientRelationInfoService patientRelationInfoService;

    private final PatientRelationInfoRepository patientRelationInfoRepository;

    private final AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    private final AppointmentTransactionRequestLogService appointmentTransactionRequestLogService;

    public AppointmentServiceImpl(PatientService patientService,
                                  DoctorService doctorService,
                                  SpecializationService specializationService,
                                  AppointmentRepository appointmentRepository,
                                  DoctorDutyRosterRepository doctorDutyRosterRepository,
                                  DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                  HospitalService hospitalService,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentRescheduleLogRepository appointmentRescheduleLogRepository,
                                  AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
                                  AppointmentReservationLogRepository appointmentReservationLogRepository,
                                  AppointmentFollowUpTrackerService appointmentFollowUpTrackerService,
                                  HospitalPatientInfoService hospitalPatientInfoService,
                                  PatientMetaInfoService patientMetaInfoService,
                                  PatientRelationInfoService patientRelationInfoService,
                                  PatientRelationInfoRepository patientRelationInfoRepository,
                                  AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
                                  AppointmentTransactionRequestLogService appointmentTransactionRequestLogService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.appointmentRepository = appointmentRepository;
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.hospitalService = hospitalService;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentRescheduleLogRepository = appointmentRescheduleLogRepository;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentReservationLogRepository = appointmentReservationLogRepository;
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
        this.hospitalPatientInfoService = hospitalPatientInfoService;
        this.patientMetaInfoService = patientMetaInfoService;
        this.patientRelationInfoService = patientRelationInfoService;
        this.patientRelationInfoRepository = patientRelationInfoRepository;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.appointmentTransactionRequestLogService = appointmentTransactionRequestLogService;
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

    /*1.SAVE IN AppointmentTransactionRequestLog WHERE TRANSACTION STATUS = 'N'.
    * SAVED INITIALLY SUCH THAT IF ANY TIMEOUT OR SERVER ISSUES OCCUR,
    * THEN WHOLE APPOINTMENT CAN BE TRACED BY ITS STATUS.
    *
    * 2. VALIDATE IF AppointmentReservationLog(TEMPORARILY RESERVED TIME SLOT) EXISTS.
    * SINCE AFTER SOME TIME, THE ROW IS RIGHT AWAY DELETED FROM THE TABLE.
    * HENCE THE SELECTED TIME SLOT IS EXPIRED AND IS NO LONGER AVAILABLE FOR APPOINTMENT.
    *
    * 3. VALIDATE REQUEST INFO :
    *   A. VALIDATE IF REQUESTED DATE AND TIME IS BEFORE CURRENT DATE AND TIME.
    *   B. VALIDATE IF ANY OTHER APPOINTMENT EXISTS ON THE SAME CRITERIA
    *   C. VALIDATE IF ANY APPOINTMENT RESERVATION EXISTS
    *   D. VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN DOCTOR DUTY ROSTER TIME SCHEDULES
    *
    * 4. SAVE Patient, PatientMetaInfo, HospitalPatientInfo
    *
    * 5. GENERATE UNIQUE APPOINTMENT NUMBER WHICH STARTS WITH '0001' AND INCREMENTS BY 1 TILL IT REACHES FISCAL YEAR.
    *    IT STARTS WITH '0001' IN NEXT FISCAL YEAR. GENERATE ON THE BASIS OF LATEST APPOINTMENT NUMBER & FISCAL YEAR.
    *
    * 6. SAVE APPOINTMENT
    *
    * 7. SAVE APPOINTMENT TRANSACTION DETAILS
    *
    * 8. IF isFollowUp = 'Y', SAVE APPOINTMENT FOLLOW UP TABLES
    *
    * 9. UPDATE TRANSACTION STATUS IN AppointmentTransactionRequestLog AS 'Y'
    * AND RETURN THE FINAL RESPONSE.
    *
    * */
    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForSelf(AppointmentRequestDTOForSelf requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentTransactionRequestLog transactionRequestLog =
                appointmentTransactionRequestLogService.save(
                        requestDTO.getTransactionInfo().getTransactionDate(),
                        requestDTO.getTransactionInfo().getTransactionNumber(),
                        requestDTO.getPatientInfo().getName()
                );

        validateAppointmentReservationIsActive(appointmentInfo.getAppointmentReservationId());

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

        if (appointmentInfo.getIsFollowUp().equals(YES))
            saveAppointmentFollowUpDetails(appointmentInfo.getParentAppointmentId(), appointment.getId());

        updateAppointmentTransactionRequestLog(transactionRequestLog);

        AppointmentSuccessResponseDTO responseDTO =
                parseToAppointmentSuccessResponseDTO(appointmentNumber, transactionRequestLog.getTransactionStatus());

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForOthers(AppointmentRequestDTOForOthers requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentTransactionRequestLog transactionRequestLog =
                appointmentTransactionRequestLogService.save(
                        requestDTO.getTransactionInfo().getTransactionDate(),
                        requestDTO.getTransactionInfo().getTransactionNumber(),
                        requestDTO.getRequestFor().getName()
                );

        validateAppointmentReservationIsActive(appointmentInfo.getAppointmentReservationId());

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

        if (appointmentInfo.getIsFollowUp().equals(YES))
            saveAppointmentFollowUpDetails(appointmentInfo.getParentAppointmentId(), appointment.getId());

        updateAppointmentTransactionRequestLog(transactionRequestLog);

        AppointmentSuccessResponseDTO responseDTO =
                parseToAppointmentSuccessResponseDTO(appointmentNumber, transactionRequestLog.getTransactionStatus());

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PENDING_APPOINTMENTS);

        List<AppointmentMinResponseDTO> pendingAppointments =
                appointmentRepository.fetchPendingAppointments(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, PENDING_APPOINTMENTS, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentMinResponseWithStatusDTO(pendingAppointments);
    }

    @Override
    public StatusResponseDTO cancelAppointment(AppointmentCancelRequestDTO cancelRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CANCELLING_PROCESS_STARTED);

        Appointment appointment = findPendingAppointmentById(cancelRequestDTO.getAppointmentId());

        parseAppointmentCancelledDetails(appointment, cancelRequestDTO.getRemarks());

        Double refundAmount = appointmentRepository.calculateRefundAmount(cancelRequestDTO.getAppointmentId());

        saveAppointmentRefundDetail(
                parseToAppointmentRefundDetail(appointment, refundAmount)
        );

        log.info(CANCELLING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return parseToStatusResponseDTO();
    }

    @Override
    public StatusResponseDTO rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

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

        return parseToStatusResponseDTO();
    }

    @Override
    public AppointmentDetailResponseWithStatusDTO fetchAppointmentDetails(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);

        AppointmentDetailResponseDTO appointmentDetails =
                appointmentRepository.fetchAppointmentDetails(appointmentId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentDetailResponseWithStatusDTO(appointmentDetails);
    }

    @Override
    public AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentMinResponseDTO> appointmentHistory =
                appointmentRepository.fetchAppointmentHistory(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentMinResponseWithStatusDTO(appointmentHistory);
    }

    @Override
    public StatusResponseDTO cancelRegistration(Long appointmentReservationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_RESERVATION_LOG);

        AppointmentReservationLog appointmentReservationLog =
                fetchAppointmentReservationLogById(appointmentReservationId);

        if (!Objects.isNull(appointmentReservationLog))
            appointmentReservationLogRepository.delete(appointmentReservationLog);

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));

        return parseToStatusResponseDTO();
    }

    @Override
    public AppointmentTransactionStatusResponseDTO fetchAppointmentTransactionStatus
            (AppointmentTransactionStatusRequestDTO requestDTO) {

        Character appointmentTransactionRequestLogStatus =
                appointmentTransactionRequestLogService.fetchAppointmentTransactionStatus
                        (requestDTO.getTransactionNumber(), requestDTO.getPatientName());

        return parseToAppointmentTransactionStatusResponseDTO(appointmentTransactionRequestLogStatus);
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPROVAL_LIST);

        AppointmentPendingApprovalResponseDTO responseDTOS =
                appointmentRepository.searchPendingVisitApprovals(searchRequestDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPROVAL_LIST, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void approveAppointment(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT);

        Appointment appointment = appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(
                appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        appointment.setStatus(APPROVED);

        saveAppointmentFollowUpTracker(appointment);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectAppointment(AppointmentRejectDTO rejectDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT);

        Appointment appointment = appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(
                rejectDTO.getAppointmentId(), getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(rejectDTO.getAppointmentId()));

        parseAppointmentRejectDetails(rejectDTO, appointment);

        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_TODAY_QUEUE);

        List<AppointmentQueueDTO> appointmentQueue = appointmentRepository.fetchAppointmentQueueLog(
                appointmentQueueRequestDTO, getLoggedInHospitalId(), pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_TODAY_QUEUE, getDifferenceBetweenTwoTime(startTime));

        return appointmentQueue;
    }

    @Override
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_RESCHEDULE_LOG);

        Map<String, List<AppointmentQueueDTO>> responseDTOS =
                appointmentRepository.fetchTodayAppointmentQueueByTime(appointmentQueueRequestDTO,
                        getLoggedInHospitalId(), pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_RESCHEDULE_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                                Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundResponseDTO refundAppointments =
                appointmentRepository.fetchRefundAppointments(searchDTO, pageable, getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public void approveRefundAppointment(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (appointmentId, getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        appointment.setStatus(REFUNDED);

        refundAppointmentDetail.setStatus(APPROVED);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId(
                        refundRejectDTO.getAppointmentId(), getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(refundRejectDTO.getAppointmentId()));

        parseRefundRejectDetails(refundRejectDTO, refundAppointmentDetail);

        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
            AppointmentStatusRequestDTO requestDTO,
            Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentStatusResponseDTO> responseDTOS =
                appointmentRepository.fetchAppointmentForAppointmentStatus(requestDTO, hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_LOG);

        AppointmentLogResponseDTO responseDTOS =
                appointmentRepository.searchAppointmentLogs(searchRequestDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                          Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_RESCHEDULE_LOG);

        AppointmentRescheduleLogResponseDTO responseDTOS =
                appointmentRepository.fetchRescheduleAppointment(rescheduleDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_RESCHEDULE_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
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
        if (appointmentCount.intValue() > 0){
            log.error(APPOINTMENT_EXISTS, convert24HourTo12HourFormat(appointmentTime));
            throw new DataDuplicationException(String.format(APPOINTMENT_EXISTS,
                    convert24HourTo12HourFormat(appointmentTime)));
        }
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
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
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

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
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

    private void saveAppointmentFollowUpDetails(Long parentAppointmentId, Long followUpAppointmentId) {

        saveAppointmentFollowUpLog(parentAppointmentId, followUpAppointmentId);

        updateAppointmentFollowUpRequestLog(parentAppointmentId);
    }

    /*RELATION BETWEEN APPOINTMENT AND ITS CONSECUTIVE APPOINTMENT LOG*/
    private void saveAppointmentFollowUpLog(Long parentAppointmentId, Long followUpAppointmentId) {

        appointmentFollowUpLogRepository.save(
                parseToAppointmentFollowUpLog(parentAppointmentId, followUpAppointmentId)
        );
    }

    /*INCREMENT APPOINTMENT FOLLOW UP REQUEST COUNT BY 1*/
    private void updateAppointmentFollowUpRequestLog(Long parentAppointmentId) {

        Long appointmentFollowUpTrackerId =
                appointmentFollowUpTrackerService.fetchByParentAppointmentId(parentAppointmentId);

        appointmentFollowUpRequestLogService.update(appointmentFollowUpTrackerId);
    }

    /*IF IS FOLLOW UP = 'N',
    *   THEN PERSIST IN AppointmentFollowUpTracker WHERE
    *   ALLOWED NUMBER OF FOLLOW UPS & INTERVAL DAYS IS BASED ON THE SELECTED HOSPITAL.
    *   PERSIST IN APPOINTMENT FOLLOW UP REQUEST WITH REQUEST COUNT STARTING WITH 0.
    *   REGISTER PATIENT AND GENERATE A UNIQUE REGISTRATION NUMBER.
    *
    * ELSE
    *   UPDATE APPOINTMENT FOLLOW UP TRACKER
    *   ie. DECREMENT NUMBER OF FOLLOW UPS BY 1 AND IF IT IS ZERO, THEN SET STATUS AS 'N'
    *   ONLY ACTIVE ('Y') APPOINTMENT FOLLOW UP TRACKER ARE FETCHED TO DIFFERENTIATE
     *  WHETHER IT IS FOLLOW UP OR NORMAL APPOINTMENT
    *   */
    private void saveAppointmentFollowUpTracker(Appointment appointment) {

        if (appointment.getIsFollowUp().equals(YES)) {
            AppointmentFollowUpLog appointmentFollowUpLog =
                    appointmentFollowUpLogRepository.findByFollowUpAppointmentId(appointment.getId())
                            .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointment.getId()));

            appointmentFollowUpTrackerService.updateFollowUpTracker(appointmentFollowUpLog.getParentAppointmentId());

        } else {
            AppointmentFollowUpTracker appointmentFollowUpTracker =
                    appointmentFollowUpTrackerService.save(
                            appointment.getId(),
                            appointment.getHospitalId(),
                            appointment.getDoctorId(),
                            appointment.getSpecializationId(),
                            appointment.getPatientId()
                    );

            appointmentFollowUpRequestLogService.save(appointmentFollowUpTracker);

            registerPatient(appointment.getPatientId().getId(), appointment.getHospitalId().getId());
        }
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
        log.error(NAME_AND_MOBILE_NUMBER_DUPLICATION_ERROR, PATIENT, name, mobileNumber, dateOfBirth);
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

        boolean isTimeValid = validateIfRequestedAppointmentTimeIsValid(doctorDutyRosterInfo, appointmentInfo);

        if (!isTimeValid){
            log.error(INVALID_APPOINTMENT_TIME, convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime()));
            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
                    convert24HourTo12HourFormat(appointmentInfo.getAppointmentTime())));
        }
    }

    private void validateAppointmentReservationIsActive(Long appointmentReservationId) {
        AppointmentReservationLog appointmentReservationLog =
                fetchAppointmentReservationLogById(appointmentReservationId);

        if (Objects.isNull(appointmentReservationLog)) {
            log.error(APPOINTMENT_FAILED_DEBUG_MESSAGE);
            throw new BadRequestException(APPOINTMENT_FAILED_MESSAGE, APPOINTMENT_FAILED_DEBUG_MESSAGE);
        }
    }

    /*VALIDATE IF APPOINTMENT ALREADY EXISTS ON SELECTED DATE AND TIME */
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

        Long appointmentCount = appointmentReservationLogRepository.validateDuplicityExceptCurrentReservationId(
                appointmentInfo.getAppointmentDate(),
                appointmentInfo.getAppointmentTime(),
                appointmentInfo.getDoctorId(),
                appointmentInfo.getSpecializationId(),
                appointmentInfo.getAppointmentReservationId()
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

        if (doctorDutyRosterInfo.getDayOffStatus().equals(YES)) {
            log.error(DOCTOR_NOT_AVAILABLE, utilDateToSqlDate(appointmentInfo.getAppointmentDate()));
            throw new NoContentFoundException(
                    String.format(DOCTOR_NOT_AVAILABLE, utilDateToSqlDate(appointmentInfo.getAppointmentDate())));
        }

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

    private AppointmentReservationLog fetchAppointmentReservationLogById(Long appointmentReservationId) {
        return appointmentReservationLogRepository.findAppointmentReservationLogById(appointmentReservationId);
    }
}

