package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.approval.AppointmentRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.approval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentMode.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.parseAppointmentRejectDetails;
import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.calculateAge;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientService patientService;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentFollowUpLogRepository appointmentFollowUpLogRepository;

    private final AppointmentFollowUpTrackerService appointmentFollowUpTrackerService;

    private final AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    private final AppointmentDoctorInfoRepository appointmentDoctorInfoRepository;

    private final IntegrationCheckPointService integrationCheckPointService;

    public AppointmentServiceImpl(PatientService patientService,
                                  AppointmentRepository appointmentRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
                                  AppointmentFollowUpTrackerService appointmentFollowUpTrackerService,
                                  AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
                                  AppointmentDoctorInfoRepository appointmentDoctorInfoRepository,
                                  IntegrationCheckPointServiceImpl integrationCheckPointService) {
        this.patientService = patientService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.appointmentDoctorInfoRepository = appointmentDoctorInfoRepository;
        this.integrationCheckPointService = integrationCheckPointService;
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPOINTMENTS);

        AppointmentPendingApprovalResponseDTO responseDTOS =
                appointmentRepository.searchPendingVisitApprovals(searchRequestDTO, pageable, getLoggedInHospitalId());

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPOINTMENTS, getDifferenceBetweenTwoTime(startTime));

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
    public void approveAppointment(IntegrationBackendRequestDTO integrationRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT);

        // isPatientStatus-->      true--> no hospital number | new registration patient
        // isPatientStatus-->      false--> hospital number   | registered patient
        Appointment appointment = fetchPendingAppointment(
                integrationRequestDTO.getAppointmentId(), getLoggedInHospitalId());

        if (integrationRequestDTO.getIntegrationChannelCode() != null)
            integrationCheckPointService.apiIntegrationCheckpointForDoctorAppointment(appointment, integrationRequestDTO);

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

        AppointmentRefundDetailResponseDTO refundAppointments =
                appointmentRepository.fetchRefundDetailsById(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public void approveRefundAppointment(IntegrationRefundRequestDTO refundRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (refundRequestDTO.getAppointmentId(), getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(refundRequestDTO.getAppointmentId()));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (refundRequestDTO.getAppointmentId(), getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(refundRequestDTO.getAppointmentId()));

        AppointmentTransactionDetail appointmentTransactionDetail =
                fetchAppointmentTransactionDetail(refundRequestDTO.getAppointmentId());

        integrationCheckPointService.apiIntegrationCheckpointForRefundAppointment(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                refundRequestDTO);


        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));
    }


    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        IntegrationRefundRequestDTO refundRequestDTO = IntegrationRefundRequestDTO.builder()
                .appointmentId(refundRejectDTO.getAppointmentId())
                .appointmentModeId(refundRejectDTO.getAppointmentModeId())
                .integrationChannelCode(refundRejectDTO.getIntegrationChannelCode())
                .featureCode(refundRejectDTO.getFeatureCode())
                .status(refundRejectDTO.getStatus())
                .remarks(refundRejectDTO.getRemarks())
                .build();


        Long appointmentId = refundRejectDTO.getAppointmentId();

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (appointmentId, getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointmentId);

        integrationCheckPointService.apiIntegrationCheckpointForRejectAppointment(
                appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                refundRequestDTO);

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

        String appointmentServiceTypeCode = searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        Long hospitalId = getLoggedInHospitalId();

        AppointmentLogResponseDTO appointmentLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                appointmentLogs = appointmentRepository.searchDoctorAppointmentLogs(
                        searchRequestDTO, pageable, hospitalId, appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                appointmentLogs = appointmentRepository.searchHospitalDepartmentAppointmentLogs(
                        searchRequestDTO, pageable, hospitalId, appointmentServiceTypeCode);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE,
                        appointmentServiceTypeCode));
        }

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return appointmentLogs;
    }

    @Override
    public TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, TRANSACTION_LOG);

        String appointmentServiceTypeCode = searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();
        Long hospitalId = getLoggedInHospitalId();

        TransactionLogResponseDTO transactionLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchDoctorTransactionLogs(
                        searchRequestDTO, pageable, hospitalId, appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchHospitalDepartmentTransactionLogs(
                        searchRequestDTO, pageable, hospitalId, appointmentServiceTypeCode);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode));
        }

        log.info(SEARCHING_PROCESS_COMPLETED, TRANSACTION_LOG, getDifferenceBetweenTwoTime(startTime));

        return transactionLogs;
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

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException(Appointment.class, "appointmentId", appointmentId.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "appointmentId", appointmentId.toString());
    };

    private Function<String, NoContentFoundException> APPOINTMENT_MODE_WITH_GIVEN_CODE_NOT_FOUND = (code) -> {
        log.error(CONTENT_NOT_FOUND_BY_CODE, APPOINTMENT_MODE, code);
        throw new NoContentFoundException(AppointmentMode.class, "code", code);
    };

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
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

            AppointmentDoctorInfo appointmentDoctorInfo = fetchAppointmentDoctorInfo(appointment.getId());

            AppointmentFollowUpTracker appointmentFollowUpTracker =
                    appointmentFollowUpTrackerService.save(
                            appointment.getId(),
                            appointment.getHospitalId(),
                            appointmentDoctorInfo.getDoctor(),
                            appointmentDoctorInfo.getSpecialization(),
                            appointment.getPatientId()
                    );

            appointmentFollowUpRequestLogService.save(appointmentFollowUpTracker);

            registerPatient(appointment.getPatientId().getId(), appointment.getHospitalId().getId());
        }
    }

    private void registerPatient(Long patientId, Long hospitalId) {
        patientService.registerPatient(patientId, hospitalId);
    }

    private AppointmentDoctorInfo fetchAppointmentDoctorInfo(Long appointmentId) {
        return appointmentDoctorInfoRepository.fetchAppointmentDoctorInfo(appointmentId)
                .orElseThrow(() -> APPOINTMENT_DOCTOR_INFO_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_DOCTOR_INFO_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_DOCTOR_INFO);
        throw new NoContentFoundException(AppointmentDoctorInfo.class, "appointmentId", appointmentId.toString());
    };

    private Appointment fetchPendingAppointment(Long appointmentId, Long hospitalId) {
        return appointmentRepository.fetchPendingAppointmentByIdAndHospitalId(appointmentId, hospitalId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }
}

