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
import com.cogent.cogentappointment.admin.dto.request.refund.EsewaRefundRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.Properties;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentPendingApproval.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpRequestLogService;
import com.cogent.cogentappointment.admin.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.persistence.model.*;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.RefundResponseConstant.*;
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
import static com.cogent.cogentappointment.admin.utils.resttemplate.IntegrationRequestHeaders.getEsewaPaymentStatusAPIHeaders;
import static com.cogent.cogentappointment.admin.utils.resttemplate.IntegrationRequestURI.ESEWA_REFUND_API;

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

    private final IntegrationRepository integrationRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                  AppointmentFollowUpTrackerService appointmentFollowUpTrackerService,
                                  PatientService patientService,
                                  AppointmentFollowUpLogRepository appointmentFollowUpLogRepository,
                                  AppointmentFollowUpRequestLogService appointmentFollowUpRequestLogService,
                                  ThirdPartyConnectorService thirdPartyConnectorService,
                                  IntegrationRepository integrationRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
        this.patientService = patientService;
        this.appointmentFollowUpLogRepository = appointmentFollowUpLogRepository;
        this.appointmentFollowUpRequestLogService = appointmentFollowUpRequestLogService;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.integrationRepository = integrationRepository;
    }


//    private final RestTemplateUtils restTemplateUtils;


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
    public void approveRefundAppointment(Long appointmentId,
                                         IntegrationBackendRequestDTO backendRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId(appointmentId)
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        //api integration logic
        ResponseEntity<?> responseEntity = null;
        if (backendRequestDTO != null) {
            responseEntity = apiIntegrationCheckpoint(backendRequestDTO);
        }

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointmentId);

        String response = processRefundRequest(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                true,backendRequestDTO);

        updateAppointmentAndAppointmentRefundDetails(response, appointment, refundAppointmentDetail, null);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO,
                                        IntegrationBackendRequestDTO backendRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_REFUND);

        Long appointmentId = refundRejectDTO.getAppointmentId();

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId
                        (appointmentId)
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentById
                (appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointmentId);

        String response = processRefundRequest(appointment,
                appointmentTransactionDetail,
                refundAppointmentDetail,
                false,backendRequestDTO);

        updateAppointmentAndAppointmentRefundDetails(response, appointment, refundAppointmentDetail, refundRejectDTO);

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

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPROVAL_LIST);

        AppointmentPendingApprovalResponseDTO responseDTOS =
                appointmentRepository.searchPendingVisitApprovals(searchRequestDTO, pageable);

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
    public void approveAppointment(Long appointmentId, IntegrationBackendRequestDTO backendRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT);

        ResponseEntity<?> integrationResponse = null;
        if (backendRequestDTO != null) {
            integrationResponse = apiIntegrationCheckpoint(backendRequestDTO);
        }

        Appointment appointment = appointmentRepository.fetchPendingAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        appointment.setStatus(APPROVED);

        saveAppointmentFollowUpTracker(appointment);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    private ResponseEntity<?> apiIntegrationCheckpoint(IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        BackendIntegrationApiInfo integrationHospitalApiInfo = getHospitalApiIntegration(integrationBackendRequestDTO);

//        integrationHospitalApiInfo.forEach(apiInfo -> {
        ResponseEntity<?> responseEntity = thirdPartyConnectorService.getHospitalService(integrationHospitalApiInfo);
//        });

        return responseEntity;

    }

    private BackendIntegrationApiInfo getHospitalApiIntegration(IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        ClientFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchClientIntegrationResponseDTOforBackendIntegration(integrationBackendRequestDTO);

//        List<BackendIntegrationApiInfo> integrationHospitalApiInfos = new ArrayList<>();

//        featureIntegrationResponse.forEach(integrationResponse -> {

        Map<String, String> requestHeaderResponse = integrationRepository.
                findApiRequestHeadersResponse(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findApiQueryParametersResponse(featureIntegrationResponse.getApiIntegrationFormatId());

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        requestHeaderResponse.forEach((key, value) -> {
            headers.add(key, value);
        });

        BackendIntegrationApiInfo hospitalApiInfo = new BackendIntegrationApiInfo();
        hospitalApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        hospitalApiInfo.setHttpHeaders(headers);

        if (!queryParametersResponse.isEmpty()) {
            hospitalApiInfo.setQueryParameters(queryParametersResponse);
        }
        hospitalApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

//            integrationHospitalApiInfos.add(hospitalApiInfo);

//        });

        return hospitalApiInfo;

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
    public Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                                                   Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_RESCHEDULE_LOG);

        Map<String, List<AppointmentQueueDTO>> responseDTOS =
                appointmentRepository.fetchTodayAppointmentQueueByTime(appointmentQueueRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_RESCHEDULE_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private String processRefundRequest(Appointment appointment,
                                        AppointmentTransactionDetail transactionDetail,
                                        AppointmentRefundDetail appointmentRefundDetail,
                                        Boolean isRefund,
                                        IntegrationBackendRequestDTO backendRequestDTO) {

        String thirdPartyResponse = null;

        switch (appointment.getAppointmentModeId().getCode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:

                //api integration
                thirdPartyResponse = requestEsewaForRefund(appointment,
                        transactionDetail,
                        appointmentRefundDetail,
                        isRefund,backendRequestDTO);
                break;

            default:
                throw new BadRequestException("APPOINTMENT MODE NOT VALID");
        }


        return thirdPartyResponse;
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
                saveAppointmentRefundDetail(parseRefundRejectDetails(refundRejectDTO, refundAppointmentDetail));
                break;

            case AMBIGIOUS:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment, refundAppointmentDetail, response);
                break;

            default:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment, refundAppointmentDetail, response);
                break;

        }
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

    private String requestEsewaForRefund(Appointment appointment,
                                         AppointmentTransactionDetail transactionDetail,
                                         AppointmentRefundDetail appointmentRefundDetail,
                                         Boolean isRefund,
                                         IntegrationBackendRequestDTO backendRequestDTO) {

//        BackendIntegrationApiInfo backendIntegrationHospitalApiInfo



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


        HttpEntity<?> request = new HttpEntity<>(esewaRefundRequestDTO, getEsewaPaymentStatusAPIHeaders());

        String url = String.format(ESEWA_REFUND_API, "5VQ");

//        thirdPartyConnectorService.getHospitalService(backendRequestDTO);

//        ResponseEntity<EsewaResponseDTO> response = (ResponseEntity<EsewaResponseDTO>) restTemplateUtils.
//                postRequest(url, request, EsewaResponseDTO.class);

//        return (response.getBody().getStatus() == null) ? AMBIGIOUS : response.getBody().getStatus();

        return null;
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
            AppointmentFollowUpTracker appointmentFollowUpTracker = appointmentFollowUpTrackerService.save(
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

    private void saveRefundDetails(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

}

