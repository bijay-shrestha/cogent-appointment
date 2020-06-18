package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentPendingApproval.AppointmentRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentCancelApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpRequestLogService;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.persistence.model.*;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_FONEPAY_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_MODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.IntegrationApiMessages.INTEGRATION_PARAMETER_IS_NULL;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.IntegrationApiMessages.INTEGRATION_PARAMETER_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseAppointmentRejectDetails;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseRefundRejectDetails;
import static com.cogent.cogentappointment.admin.utils.RefundStatusUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.AgeConverterUtils.calculateAge;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final AppointmentFollowUpTrackerService appointmentFollowUpTrackerService;

    private final PatientService patientService;

    private final AppointmentFollowUpLogRepository appointmentFollowUpLogRepository;

    private final AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService;

    private final ThirdPartyConnectorService thirdPartyConnectorService;

    private final IntegrationCheckpointImpl integrationCheckpointService;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final AppointmentDoctorInfoRepository appointmentDoctorInfoRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                  AppointmentFollowUpTrackerService appointmentFollowUpTrackerService,
                                  PatientService patientService,
                                  AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
                                  AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
                                  ThirdPartyConnectorService thirdPartyConnectorService,
                                  IntegrationCheckpointImpl integrationCheckpointService,
                                  HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                  AppointmentDoctorInfoRepository appointmentDoctorInfoRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
        this.patientService = patientService;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.integrationCheckpointService = integrationCheckpointService;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentDoctorInfoRepository = appointmentDoctorInfoRepository;
    }

    @Override
    public AppointmentRefundResponseDTO fetchAppointmentCancelApprovals(AppointmentCancelApprovalSearchDTO searchDTO,
                                                                        Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundResponseDTO refundAppointments =
                appointmentRepository.fetchAppointmentCancelApprovals(searchDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetailResponseDTO refundAppointments = appointmentRepository.fetchRefundDetailsById(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public void approveRefundAppointment(IntegrationBackendRequestDTO backendRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId(backendRequestDTO.getAppointmentId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(backendRequestDTO.getAppointmentId()));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentById(backendRequestDTO.getAppointmentId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(backendRequestDTO.getAppointmentId()));

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(backendRequestDTO.getAppointmentId());

        ThirdPartyResponse response = processRefundRequest(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                true, backendRequestDTO);

        if (!Objects.isNull(response.getCode())) {
            throw new BadRequestException(response.getMessage(), response.getMessage());
        }

        updateAppointmentAndAppointmentRefundDetails(response.getStatus(), appointment, refundAppointmentDetail, null);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_REFUND);


        IntegrationBackendRequestDTO backendRequestDTO=IntegrationBackendRequestDTO.builder()
                .appointmentId(refundRejectDTO.getAppointmentId())
                .build();

        Long appointmentId = refundRejectDTO.getAppointmentId();

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId
                        (appointmentId)
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentById
                (appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointmentId);

        if (backendRequestDTO != null) {
            ThirdPartyResponse response = processRefundRequest(appointment,
                    appointmentTransactionDetail,
                    refundAppointmentDetail,
                    false, backendRequestDTO);

            if (!Objects.isNull(response.getCode())) {
                throw new BadRequestException(response.getMessage());
            }

            updateAppointmentAndAppointmentRefundDetails(response.getStatus(),
                    appointment,
                    refundAppointmentDetail,
                    refundRejectDTO);

        } else {
            throw new BadRequestException(INTEGRATION_PARAMETER_NOT_FOUND, INTEGRATION_PARAMETER_IS_NULL);
        }


        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
            AppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentStatusResponseDTO> responseDTOS =
                appointmentRepository.fetchAppointmentForAppointmentStatus(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPOINTMENT_APPROVAL);

        AppointmentPendingApprovalResponseDTO responseDTOS =
                appointmentRepository.searchPendingVisitApprovals(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPOINTMENT_APPROVAL, getDifferenceBetweenTwoTime(startTime));

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
    public void approveAppointment(IntegrationBackendRequestDTO backendRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT);

        // isPatientStatus-->      true--> no hospital number | new registration patient
        // isPatientStatus-->      false--> hospital number   | registered patient

        Appointment appointment = appointmentRepository.fetchPendingAppointmentById(backendRequestDTO.getAppointmentId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(backendRequestDTO.getAppointmentId()));

        if (backendRequestDTO.getIntegrationChannelCode() != null) {
            integrationCheckpointService.apiIntegrationCheckpoint(appointment, backendRequestDTO);
        }


        appointment.setStatus(APPROVED);

        saveAppointmentFollowUpTracker(appointment);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }




    @Override
    public void rejectAppointment(AppointmentRejectDTO rejectDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT);

        Appointment appointment = appointmentRepository.fetchPendingAppointmentById(
                rejectDTO.getAppointmentId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(rejectDTO.getAppointmentId()));

        parseAppointmentRejectDetails(rejectDTO, appointment);

        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_LOG);

        AppointmentLogResponseDTO responseDTOS =
                appointmentRepository.searchAppointmentLogs(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, TRANSACTION_LOG);

        TransactionLogResponseDTO responseDTOS = appointmentRepository.searchTransactionLogs(searchRequestDTO,
                pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, TRANSACTION_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentRescheduleLogResponseDTO fetchRescheduleAppointment(AppointmentRescheduleLogSearchDTO rescheduleDTO,
                                                                          Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_RESCHEDULE_LOG);

        AppointmentRescheduleLogResponseDTO responseDTOS =
                appointmentRepository.fetchRescheduleAppointment(rescheduleDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_RESCHEDULE_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<AppointmentQueueDTO> fetchAppointmentQueueLog(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                              Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_TODAY_QUEUE);

        List<AppointmentQueueDTO> responseDTOS =
                appointmentRepository.fetchAppointmentQueueLog(appointmentQueueRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_TODAY_QUEUE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_RESCHEDULE_LOG);

        Map<String, List<AppointmentQueueDTO>> responseDTOS =
                appointmentRepository.fetchTodayAppointmentQueueByTime(appointmentQueueRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_RESCHEDULE_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
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

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetail(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT, appointmentId);
        throw new NoContentFoundException(Appointment.class, "appointmentId", appointmentId.toString());
    };

    private void saveAppointmentFollowUpTracker(Appointment appointment) {

        if (appointment.getIsFollowUp().equals(YES)) {
            AppointmentFollowUpLog appointmentFollowUpLog =
                    appointmentFollowUpLogRepository.findByFollowUpAppointmentId(appointment.getId())
                            .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointment.getId()));

            appointmentFollowUpTrackerService.updateFollowUpTracker(appointmentFollowUpLog.getParentAppointmentId());

        } else {

            AppointmentDoctorInfo appointmentDoctorInfo = fetchAppointmentDoctorInfo(appointment.getId());

            AppointmentFollowUpTracker appointmentFollowUpTracker = appointmentFollowUpTrackerService.save(
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

    private void saveRefundDetails(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private ThirdPartyResponse processRefundRequest(Appointment appointment,
                                                    AppointmentTransactionDetail transactionDetail,
                                                    AppointmentRefundDetail appointmentRefundDetail,
                                                    Boolean isRefund,
                                                    IntegrationBackendRequestDTO backendRequestDTO) {

        ThirdPartyResponse thirdPartyResponse = null;
        switch (appointment.getAppointmentModeId().getCode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:

                //esewa integration
                thirdPartyResponse = integrationCheckpointService.processEsewaRefundRequest(appointment,
                        transactionDetail,
                        appointmentRefundDetail,
                        isRefund, backendRequestDTO);
                break;
            case APPOINTMENT_MODE_FONEPAY_CODE:
                break;

            default:
                throw new BadRequestException(INVALID_APPOINTMENT_MODE);
        }


        return thirdPartyResponse;
    }

    private void updateAppointmentAndAppointmentRefundDetails(String response,
                                                              Appointment appointment,
                                                              AppointmentRefundDetail refundAppointmentDetail,
                                                              AppointmentRefundRejectDTO refundRejectDTO) {
        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        refundAppointmentDetail,
                        response);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        refundAppointmentDetail,
                        response);
                break;

            case SUCCESS:
                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRejectDTO,
                        refundAppointmentDetail));
                break;

            case AMBIGUOUS:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment,
                        refundAppointmentDetail,
                        response);
                throw new BadRequestException(response, response);

            default:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment,
                        refundAppointmentDetail,
                        response);
                throw new BadRequestException(response, response);
        }
    }


    private AppointmentDoctorInfo fetchAppointmentDoctorInfo(Long appointmentId) {
        return appointmentDoctorInfoRepository.fetchAppointmentDoctorInfo(appointmentId)
                .orElseThrow(() -> APPOINTMENT_DOCTOR_INFO_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_DOCTOR_INFO_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_DOCTOR_INFO);
        throw new NoContentFoundException(AppointmentDoctorInfo.class, "appointmentId", appointmentId.toString());
    };

}

