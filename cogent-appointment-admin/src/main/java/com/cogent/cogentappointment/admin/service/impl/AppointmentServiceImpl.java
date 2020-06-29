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
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationRefundRequestDTO;
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
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseAppointmentRejectDetails;
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

    private final IntegrationCheckPointService integrationCheckpointService;

    private final AppointmentDoctorInfoRepository appointmentDoctorInfoRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                  AppointmentFollowUpTrackerService appointmentFollowUpTrackerService,
                                  PatientService patientService,
                                  AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
                                  AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
                                  IntegrationCheckPointService integrationCheckpointService,
                                  AppointmentDoctorInfoRepository appointmentDoctorInfoRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
        this.patientService = patientService;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.integrationCheckpointService = integrationCheckpointService;
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
    public void approveRefundAppointment(IntegrationRefundRequestDTO refundRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId(refundRequestDTO.getAppointmentId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(
                                refundRequestDTO.getAppointmentId()));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentById(
                refundRequestDTO.getAppointmentId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(
                        refundRequestDTO.getAppointmentId()));

        AppointmentTransactionDetail appointmentTransactionDetail =
                fetchAppointmentTransactionDetail(refundRequestDTO.getAppointmentId());

        integrationCheckpointService.apiIntegrationCheckpointForRefundAppointment(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                refundRequestDTO);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_REFUND);

        IntegrationRefundRequestDTO refundRequestDTO = IntegrationRefundRequestDTO.builder()
                .appointmentId(refundRejectDTO.getAppointmentId())
                .hospitalId(refundRejectDTO.getHospitalId())
                .appointmentModeId(refundRejectDTO.getAppointmentModeId())
                .featureCode(refundRejectDTO.getFeatureCode())
                .integrationChannelCode(refundRejectDTO.getIntegrationChannelCode())
                .remarks(refundRejectDTO.getRemarks())
                .status(refundRejectDTO.getStatus())
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

        integrationCheckpointService.apiIntegrationCheckpointForRejectAppointment(
                appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                refundRequestDTO);

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
            integrationCheckpointService.apiIntegrationCheckpointForDoctorAppointment(appointment, backendRequestDTO);
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

        String appointmentServiceTypeCode = searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        AppointmentLogResponseDTO appointmentLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                appointmentLogs = appointmentRepository.searchDoctorAppointmentLogs(
                        searchRequestDTO, pageable, appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                appointmentLogs = appointmentRepository.searchHospitalDepartmentAppointmentLogs(
                        searchRequestDTO, pageable, appointmentServiceTypeCode);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode));
        }

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return appointmentLogs;
    }

    @Override
    public TransactionLogResponseDTO searchTransactionLogs(TransactionLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, TRANSACTION_LOG);

        String appointmentServiceTypeCode = Objects.isNull(searchRequestDTO.getAppointmentServiceTypeCode()) ?
                DOCTOR_CONSULTATION_CODE : searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        TransactionLogResponseDTO transactionLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchDoctorAppointmentTransactionLogs(
                        searchRequestDTO, pageable, appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchHospitalDepartmentTransactionLogs(
                        searchRequestDTO, pageable, appointmentServiceTypeCode);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE,
                        appointmentServiceTypeCode));
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

    private AppointmentDoctorInfo fetchAppointmentDoctorInfo(Long appointmentId) {
        return appointmentDoctorInfoRepository.fetchAppointmentDoctorInfo(appointmentId)
                .orElseThrow(() -> APPOINTMENT_DOCTOR_INFO_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_DOCTOR_INFO_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_DOCTOR_INFO);
        throw new NoContentFoundException(AppointmentDoctorInfo.class, "appointmentId", appointmentId.toString());
    };

}

