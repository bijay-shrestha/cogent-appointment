package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.cancel.AppointmentCancelRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentTransactionStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentRequestDTOForOthers;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentRequestDTOForSelf;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.save.AppointmentTransactionRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientRequestForDTO;
import com.cogent.cogentappointment.client.dto.request.refund.EsewaRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.Properties;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.*;
import com.cogent.cogentappointment.client.dto.response.appointment.esewa.history.AppointmentResponseWithStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AppointmentServiceMessage.*;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_APPOINTMENT_CHARGE_INVALID;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.DoctorServiceMessages.DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentMode.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.client.log.constants.AppointmentReservationLogConstant.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.client.log.constants.PatientLog.PATIENT;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpLogUtils.parseToAppointmentFollowUpLog;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionDetailUtils.parseToAppointmentTransactionInfo;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionRequestLogUtils.parseToAppointmentTransactionStatusResponseDTO;
import static com.cogent.cogentappointment.client.utils.AppointmentTransactionRequestLogUtils.updateAppointmentTransactionRequestLog;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.calculateAge;
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

    private final AppointmentModeRepository appointmentModeRepository;

    private final AppointmentStatisticsRepository appointmentStatisticsRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final Validator validator;

    private final IntegrationRepository integrationRepository;

    private final ThirdPartyConnectorService thirdPartyConnectorService;

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
                                  AppointmentTransactionRequestLogService appointmentTransactionRequestLogService,
                                  AppointmentModeRepository appointmentModeRepository,
                                  AppointmentStatisticsRepository appointmentStatisticsRepository,
                                  HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                  Validator validator,
                                  IntegrationRepository integrationRepository,
                                  ThirdPartyConnectorService thirdPartyConnectorService) {
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
        this.appointmentModeRepository = appointmentModeRepository;
        this.appointmentStatisticsRepository = appointmentStatisticsRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.validator = validator;
        this.integrationRepository = integrationRepository;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
    }

    @Override
    public AppointmentCheckAvailabilityResponseDTO fetchAvailableTimeSlots(AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        validateIfRequestIsPastDate(requestDTO.getAppointmentDate());

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        AppointmentCheckAvailabilityResponseDTO responseDTO =
                fetchAvailableTimeSlots(doctorDutyRosterInfo, requestDTO);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public AppointmentCheckAvailabilityResponseDTO fetchCurrentAvailableTimeSlots
            (AppointmentCheckAvailabilityRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);

        validateIfRequestIsPastDate(requestDTO.getAppointmentDate());

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo = fetchDoctorDutyRosterInfo(
                requestDTO.getAppointmentDate(), requestDTO.getDoctorId(), requestDTO.getSpecializationId());

        AppointmentCheckAvailabilityResponseDTO responseDTO =
                fetchCurrentAvailableTimeSlots(doctorDutyRosterInfo, requestDTO);

        log.info(CHECK_AVAILABILITY_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    /*1.SAVE IN AppointmentTransactionRequestLog WHERE TRANSACTION STATUS = 'N'.
    * SAVED INITIALLY SUCH THAT IF ANY TIMEOUT OR SERVER ISSUES OCCUR,
    * THEN WHOLE APPOINTMENT CAN BE TRACED BY ITS STATUS.
    *
    * 2. VALIDATE IF AppointmentReservationLog(TEMPORARILY RESERVED TIME SLOT) EXISTS/STILL ACTIVE.
    * SINCE AFTER SOME TIME, THE ROW IS RIGHT AWAY DELETED FROM THE TABLE.
    * AND HENCE THE SELECTED TIME SLOT IS EXPIRED AND IS NO LONGER AVAILABLE FOR APPOINTMENT.
    *
    * 3. VALIDATE REQUEST INFO :
    *   A. VALIDATE IF ANY OTHER APPOINTMENT EXISTS ON THE SAME CRITERIA
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
    * */
    @Override
    public AppointmentSuccessResponseDTO saveAppointmentForSelf(@Valid AppointmentRequestDTOForSelf requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        validateConstraintViolation(validator.validate(requestDTO));

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentTransactionRequestLog transactionRequestLog =
                appointmentTransactionRequestLogService.save(
                        requestDTO.getTransactionInfo().getTransactionDate(),
                        requestDTO.getTransactionInfo().getTransactionNumber(),
                        requestDTO.getPatientInfo().getName()
                );

        AppointmentMode appointmentMode = fetchActiveAppointmentModeIdByCode
                (requestDTO.getTransactionInfo().getAppointmentModeCode());

        AppointmentReservationLog appointmentReservationLog =
                validateAppointmentReservationIsActive(appointmentInfo.getAppointmentReservationId());

        validateIfParentAppointmentExists(appointmentReservationLog);

        validateAppointmentAmount(appointmentReservationLog.getDoctorId(),
                appointmentReservationLog.getHospitalId(),
                appointmentInfo.getIsFollowUp(),
                requestDTO.getTransactionInfo().getAppointmentAmount()
        );

        Hospital hospital = fetchHospital(appointmentReservationLog.getHospitalId());

        Patient patient = fetchPatientForSelf(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getPatientInfo()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentReservationLog.getHospitalId()
        );

        Appointment appointment = parseToAppointment(
                requestDTO.getAppointmentInfo(),
                appointmentReservationLog,
                appointmentNumber,
                YES,
                patient,
                fetchSpecialization(appointmentReservationLog.getSpecializationId(),
                        appointmentReservationLog.getHospitalId()),
                fetchDoctor(appointmentReservationLog.getDoctorId(),
                        appointmentReservationLog.getHospitalId()),
                hospital,
                appointmentMode
        );

        save(appointment);

        saveAppointmentStatistics(appointmentInfo, appointment, hospital);

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
    public AppointmentSuccessResponseDTO saveAppointmentForOthers(@Valid AppointmentRequestDTOForOthers requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        validateConstraintViolation(validator.validate(requestDTO));

        AppointmentRequestDTO appointmentInfo = requestDTO.getAppointmentInfo();

        AppointmentMode appointmentMode = fetchActiveAppointmentModeIdByCode
                (requestDTO.getTransactionInfo().getAppointmentModeCode());

        AppointmentTransactionRequestLog transactionRequestLog =
                appointmentTransactionRequestLogService.save(
                        requestDTO.getTransactionInfo().getTransactionDate(),
                        requestDTO.getTransactionInfo().getTransactionNumber(),
                        requestDTO.getRequestFor().getName()
                );

        AppointmentReservationLog appointmentReservationLog =
                validateAppointmentReservationIsActive(appointmentInfo.getAppointmentReservationId());

        validateIfParentAppointmentExists(appointmentReservationLog);

        validateAppointmentAmount(appointmentReservationLog.getDoctorId(),
                appointmentReservationLog.getHospitalId(),
                appointmentInfo.getIsFollowUp(),
                requestDTO.getTransactionInfo().getAppointmentAmount()
        );

        Hospital hospital = fetchHospital(appointmentReservationLog.getHospitalId());

        Patient patient = fetchPatientForOthers(
                appointmentInfo.getIsNewRegistration(),
                appointmentInfo.getPatientId(),
                hospital,
                requestDTO.getRequestBy(),
                requestDTO.getRequestFor()
        );

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentInfo.getCreatedDateNepali(),
                appointmentReservationLog.getHospitalId()
        );

        Appointment appointment = parseToAppointment(
                appointmentInfo,
                appointmentReservationLog,
                appointmentNumber,
                NO,
                patient,
                fetchSpecialization(appointmentReservationLog.getSpecializationId(),
                        appointmentReservationLog.getHospitalId()),
                fetchDoctor(appointmentReservationLog.getDoctorId(),
                        appointmentReservationLog.getHospitalId()),
                hospital,
                appointmentMode
        );

        save(appointment);

        saveAppointmentStatistics(appointmentInfo, appointment, hospital);

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
    public AppointmentMinResponseWithStatusDTO fetchPendingAppointments(AppointmentHistorySearchDTO searchDTO) {
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

        validateRequestedRescheduleInfo(rescheduleRequestDTO, appointment);

        saveAppointmentRescheduleLog(appointment, rescheduleRequestDTO);

        updateAppointmentDetails(appointment, rescheduleRequestDTO);

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
    public AppointmentMinResponseWithStatusDTO fetchAppointmentHistory(AppointmentHistorySearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentMinResponseDTO> appointmentHistory =
                appointmentRepository.fetchAppointmentHistory(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentMinResponseWithStatusDTO(appointmentHistory);
    }

    @Override
    public AppointmentResponseWithStatusDTO searchAppointments(AppointmentSearchDTO searchDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        AppointmentResponseWithStatusDTO appointments = searchDTO.getIsSelf().equals(YES)
                ? appointmentRepository.searchAppointmentsForSelf(searchDTO)
                : appointmentRepository.searchAppointmentsForOthers(searchDTO);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return appointments;
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
    public AppointmentPendingApprovalDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PENDING_APPROVAL_DETAIL);

        AppointmentPendingApprovalDetailResponseDTO responseDTOS =
                appointmentRepository.fetchDetailsByAppointmentId(appointmentId);

        responseDTOS.setPatientAge(calculateAge(responseDTOS.getPatientDob()));

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PENDING_APPROVAL_DETAIL, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void approveAppointment(Long appointmentId,
                                   IntegrationBackendRequestDTO integrationBackendRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT);

        if (integrationBackendRequestDTO != null) {
            apiIntegrationCheckpoint(integrationBackendRequestDTO);
        }

        Appointment appointment = appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(
                appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        appointment.setStatus(APPROVED);

        saveAppointmentFollowUpTracker(appointment);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }


    private void apiIntegrationCheckpoint(IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        List<BackendIntegrationApiInfo> integrationHospitalApiInfo = getHospitalApiIntegration(integrationBackendRequestDTO);

        integrationHospitalApiInfo.forEach(apiInfo -> {
            ResponseEntity<?> responseEntity = thirdPartyConnectorService.getHospitalService(apiInfo);
        });

    }

    private List<BackendIntegrationApiInfo> getHospitalApiIntegration(IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        List<FeatureIntegrationResponse> featureIntegrationResponse = integrationRepository.
                fetchClientIntegrationResponseDTOforBackendIntegration(integrationBackendRequestDTO);

        List<BackendIntegrationApiInfo> integrationHospitalApiInfos = new ArrayList<>();

        featureIntegrationResponse.forEach(integrationResponse -> {

            Map<String, String> requestHeaderResponse = integrationRepository.
                    findApiRequestHeaders(integrationResponse.getApiIntegrationFormatId());

            Map<String, String> queryParametersResponse = integrationRepository.
                    findApiQueryParameters(integrationResponse.getApiIntegrationFormatId());

            //headers
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            requestHeaderResponse.forEach((key, value) -> {
                headers.add(key, value);
            });

            BackendIntegrationApiInfo hospitalApiInfo = new BackendIntegrationApiInfo();
            hospitalApiInfo.setApiUri(integrationResponse.getUrl());
            hospitalApiInfo.setHttpHeaders(headers);

            if (!queryParametersResponse.isEmpty()) {
                hospitalApiInfo.setQueryParameters(queryParametersResponse);
            }
            hospitalApiInfo.setHttpMethod(integrationResponse.getRequestMethod());

            integrationHospitalApiInfos.add(hospitalApiInfo);

        });

        return integrationHospitalApiInfos;

    }

    @Override
    public void rejectAppointment(AppointmentRejectDTO rejectDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT);

        Appointment appointment = appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(
                rejectDTO.getAppointmentId(), getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(rejectDTO.getAppointmentId()));

        save(parseAppointmentRejectDetails(rejectDTO, appointment));

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
    public AppointmentRefundResponseDTO fetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO,
                                                                        Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        AppointmentRefundResponseDTO refundAppointments =
                appointmentRepository.fetchAppointmentCancelApprovals(searchDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        AppointmentRefundDetailResponseDTO refundAppointments = appointmentRepository.fetchRefundDetailsById(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public void approveRefundAppointment(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);


        //TO DO Dynamic Backend Admin Integration...


        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (appointmentId, getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointmentId);

        String response = processRefundRequest(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                true);

        updateAppointmentAndAppointmentRefundDetails(response, appointment, refundAppointmentDetail, null);


        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));
    }


    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        Long appointmentId = refundRejectDTO.getAppointmentId();

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (appointmentId, getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointmentId);

        String response = processRefundRequest(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                false);

        updateAppointmentAndAppointmentRefundDetails(response, appointment, refundAppointmentDetail, refundRejectDTO);

        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));
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
    public TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, TRANSACTION_LOG);

        TransactionLogResponseDTO responseDTOS =
                appointmentRepository.searchTransactionLogs(searchRequestDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, TRANSACTION_LOG, getDifferenceBetweenTwoTime(startTime));

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

    /*IF DOCTOR DAY OFF STATUS = 'Y', THEN THERE ARE NO AVAILABLE TIME SLOTS
* ELSE, CALCULATE AVAILABLE TIME SLOTS BASED ON DOCTOR DUTY ROSTER AND APPOINTMENT FOR THE SELECTED CRITERIA
* (APPOINTMENT DATE, DOCTOR AND SPECIALIZATION)
* FETCH APPOINTMENT RESERVATIONS FOR THE SELECTED CRITERIA AND FINALLY FILTER OUT ONLY AVAILABLE TIME SLOTS
* (EXCLUDING APPOINTMENT RESERVATION)*/
    private AppointmentCheckAvailabilityResponseDTO fetchCurrentAvailableTimeSlots(
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

            List<String> availableTimeSlots = filterCurrentDoctorTimeWithAppointments(
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

    private List<String> filterCurrentDoctorTimeWithAppointments(Duration rosterGapDuration,
                                                                 String doctorStartTime,
                                                                 String doctorEndTime,
                                                                 AppointmentCheckAvailabilityRequestDTO requestDTO) {

        List<AppointmentBookedTimeResponseDTO> bookedAppointments =
                appointmentRepository.fetchBookedAppointments(requestDTO);

        return calculateCurrentAvailableTimeSlots(
                doctorStartTime, doctorEndTime, rosterGapDuration, bookedAppointments, requestDTO.getAppointmentDate());
    }

    private List<String> fetchBookedAppointmentReservations
            (AppointmentCheckAvailabilityRequestDTO requestDTO) {
        return appointmentReservationLogRepository.fetchBookedAppointmentReservations(requestDTO);
    }

    private void filterAvailableSlotsWithAppointmentReservation(List<String> availableTimeSlots,
                                                                List<String> appointmentReservations) {
        availableTimeSlots.removeAll(appointmentReservations);
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

    private void changeAppointmentAndAppointmentRefundDetailStatus(Appointment appointment,
                                                                   AppointmentRefundDetail refundAppointmentDetail,
                                                                   String remarks) {

        save(changeAppointmentStatus.apply(appointment, remarks));

        saveAppointmentRefundDetail(changeAppointmentRefundDetailStatus.apply(refundAppointmentDetail, remarks));

    }

    private void defaultAppointmentAndAppointmentRefundDetailStatusChanges(Appointment appointment,
                                                                           AppointmentRefundDetail refundAppointmentDetail,
                                                                           String remarks) {

        save(defaultAppointmentStatusChange.apply(appointment, remarks));

        saveAppointmentRefundDetail(defaultAppointmentRefundDetailStatusChange.apply(refundAppointmentDetail, remarks));

    }

    private String processRefundRequest(Appointment appointment,
                                        AppointmentTransactionDetail transactionDetail,
                                        AppointmentRefundDetail appointmentRefundDetail,
                                        Boolean isRefund) {

        String thirdPartyResponse = null;

        switch (appointment.getAppointmentModeId().getCode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:
                thirdPartyResponse = requestEsewaForRefund(appointment,
                        transactionDetail,
                        appointmentRefundDetail,
                        isRefund);
                break;

            default:
                throw new BadRequestException("APPOINTMENT MODE NOT VALID");
        }


        return thirdPartyResponse;
    }


    private String requestEsewaForRefund(Appointment appointment,
                                         AppointmentTransactionDetail transactionDetail,
                                         AppointmentRefundDetail appointmentRefundDetail,
                                         Boolean isRefund) {
        String esewaId = appointment.getPatientId().getESewaId();

        String merchentCode = appointment.getHospitalId().getEsewaMerchantCode();

        EsewaRefundRequestDTO esewaRefundRequestDTO = EsewaRefundRequestDTO.builder()
                .esewa_id("9841409090")
                .is_refund(isRefund)
                .refund_amount(800D)
                .product_code("testBir")
                .remarks("refund")
                .txn_amount(1000D)
                .properties(Properties.builder()
                        .appointmentId(10L)
                        .hospitalName("Bir hospital")
                        .build())
                .build();


//        HttpEntity<?> request = new HttpEntity<>(esewaRefundRequestDTO,
//                getEsewaHeader(parseToHmacRequestForEsewaDTO.apply("9841409090", "testBir")));

//        HttpEntity<?> request = new HttpEntity<>(esewaRefundRequestDTO,
//                getEsewaPaymentStatusAPIHeaders());
//
//        String url = String.format(ESEWA_REFUND_API, "5VO");

//        ResponseEntity<EsewaResponseDTO> response = (ResponseEntity<EsewaResponseDTO>) restTemplateUtils.
//                postRequest(url, request, EsewaResponseDTO.class);

//        return (response.getBody().getStatus() == null) ? AMBIGIOUS : response.getBody().getStatus();

        return null;

    }

    private void updateAppointmentAndAppointmentRefundDetails(String response,
                                                              Appointment appointment,
                                                              AppointmentRefundDetail refundAppointmentDetail,
                                                              AppointmentRefundRejectDTO refundRejectDTO) {
        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment, refundAppointmentDetail, response);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment, refundAppointmentDetail, response);
                break;

            case SUCCESS:
                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRejectDTO.getRemarks(), refundAppointmentDetail));
                break;

            case AMBIGIOUS:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment, refundAppointmentDetail, response);
                break;

            default:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment, refundAppointmentDetail, response);
                break;

        }
    }

    private Appointment findPendingAppointmentById(Long appointmentId) {
        return appointmentRepository.fetchPendingAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentMode fetchActiveAppointmentModeIdByCode(String appointmentModeCode) {
        return appointmentModeRepository.fetchActiveAppointmentModeByCode(appointmentModeCode)
                .orElseThrow(() -> APPOINTMENT_MODE_WITH_GIVEN_CODE_NOT_FOUND.apply(appointmentModeCode));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_TRANSACTION_DETAIL, id);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, id);
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private Function<String, NoContentFoundException> APPOINTMENT_MODE_WITH_GIVEN_CODE_NOT_FOUND = (code) -> {
        log.error(CONTENT_NOT_FOUND_BY_CODE, APPOINTMENT_MODE, code);
        throw new NoContentFoundException(AppointmentMode.class, "code", code);
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

        AppointmentRescheduleLog appointmentRescheduleLog =
                appointmentRescheduleLogRepository.findByAppointmentId(appointment.getId());

        appointmentRescheduleLog = Objects.isNull(appointmentRescheduleLog)
                ? parseToAppointmentRescheduleLog(appointment, rescheduleRequestDTO, new AppointmentRescheduleLog())
                : parseToAppointmentRescheduleLog(appointment, rescheduleRequestDTO, appointmentRescheduleLog);

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

    private AppointmentReservationLog validateAppointmentReservationIsActive(Long appointmentReservationId) {
        AppointmentReservationLog appointmentReservationLog =
                fetchAppointmentReservationLogById(appointmentReservationId);

        if (Objects.isNull(appointmentReservationLog))
            throw new BadRequestException(APPOINTMENT_FAILED_MESSAGE, APPOINTMENT_FAILED_DEBUG_MESSAGE);

        return appointmentReservationLog;
    }

    /*VALIDATE IF APPOINTMENT ALREADY EXISTS ON SELECTED DATE AND TIME */
    private void validateIfParentAppointmentExists(AppointmentReservationLog appointmentReservationLog) {

        String appointmentTime = getTimeFromDate(appointmentReservationLog.getAppointmentTime());

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                appointmentReservationLog.getAppointmentDate(),
                appointmentTime,
                appointmentReservationLog.getDoctorId(),
                appointmentReservationLog.getSpecializationId()
        );

        validateAppointmentExists(appointmentCount, appointmentTime);
    }

    private void validateAppointmentExists(Long appointmentCount, String appointmentTime) {
        if (appointmentCount.intValue() > 0) {
            log.error(APPOINTMENT_EXISTS, convert24HourTo12HourFormat(appointmentTime));
            throw new DataDuplicationException(String.format(APPOINTMENT_EXISTS,
                    convert24HourTo12HourFormat(appointmentTime)));
        }
    }

    private void validateAppointmentAmount(Long doctorId, Long hospitalId,
                                           Character isFollowUp, Double appointmentAmount) {

        Double actualAppointmentCharge = isFollowUp.equals(YES)
                ? doctorService.fetchDoctorFollowupAppointmentCharge(doctorId, hospitalId)
                : doctorService.fetchDoctorAppointmentCharge(doctorId, hospitalId);

        if (!(Double.compare(actualAppointmentCharge, appointmentAmount) == 0)) {
            log.error(String.format(DOCTOR_APPOINTMENT_CHARGE_INVALID, appointmentAmount));
            throw new BadRequestException(String.format(DOCTOR_APPOINTMENT_CHARGE_INVALID, appointmentAmount),
                    DOCTOR_APPOINTMENT_CHARGE_INVALID_DEBUG_MESSAGE);
        }
    }

    private void saveAppointmentStatistics(AppointmentRequestDTO appointmentInfo,
                                           Appointment appointment,
                                           Hospital hospital) {
        if (Objects.isNull(appointmentInfo.getPatientId())) {
            saveAppointmentStatistics(parseAppointmentStatisticsForNew(appointment));
        } else {
            checkForRegisteredPatient(appointmentInfo, appointment, hospital);
        }
    }

    private void checkForRegisteredPatient(AppointmentRequestDTO appointmentInfo,
                                           Appointment appointment,
                                           Hospital hospital) {

        Long patientId = hospitalPatientInfoRepository.checkIfPatientIsRegistered(
                appointmentInfo.getPatientId(),
                hospital.getId()
        );

        if (Objects.isNull(patientId)) {
            saveAppointmentStatistics(parseAppointmentStatisticsForNew(appointment));
        } else {
            saveAppointmentStatistics(parseAppointmentStatisticsForRegistered(appointment));
        }
    }

    private void saveAppointmentStatistics(AppointmentStatistics appointmentStatistics) {
        appointmentStatisticsRepository.save(appointmentStatistics);
    }

    private AppointmentReservationLog fetchAppointmentReservationLogById(Long appointmentReservationId) {
        return appointmentReservationLogRepository.findAppointmentReservationLogById(appointmentReservationId);
    }

    private void saveRefundDetails(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private void validateRequestedRescheduleInfo(AppointmentRescheduleRequestDTO rescheduleRequestDTO,
                                                 Appointment appointment) {

        validateIfRequestIsBeforeCurrentDateTime(
                rescheduleRequestDTO.getRescheduleDate(), rescheduleRequestDTO.getRescheduleTime());

        Long appointmentCount = appointmentRepository.validateIfAppointmentExists(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime(),
                appointment.getDoctorId().getId(),
                appointment.getSpecializationId().getId()
        );

        validateAppointmentExists(appointmentCount, rescheduleRequestDTO.getRescheduleTime());

        DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo =
                fetchDoctorDutyRosterInfo(rescheduleRequestDTO.getRescheduleDate(),
                        appointment.getDoctorId().getId(),
                        appointment.getSpecializationId().getId()
                );

        boolean isTimeValid = validateIfRequestedAppointmentTimeIsValid(
                doctorDutyRosterInfo, rescheduleRequestDTO.getRescheduleTime()
        );

        if (!isTimeValid) {
            log.error(INVALID_APPOINTMENT_TIME, convert24HourTo12HourFormat(rescheduleRequestDTO.getRescheduleTime()));
            throw new NoContentFoundException(String.format(INVALID_APPOINTMENT_TIME,
                    convert24HourTo12HourFormat(rescheduleRequestDTO.getRescheduleTime())));
        }
    }

}

