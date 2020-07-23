package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentTransactionDetailRepository;
import com.cogent.cogentappointment.admin.service.IntegrationCheckPointService;
import com.cogent.cogentappointment.admin.service.RefundStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.APPOINTMENT_HAS_BEEN_REJECTED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.REJECTED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransactionDetailLog.APPOINTMENT_TRANSACTION_DETAIL;
import static com.cogent.cogentappointment.admin.log.constants.RefundStatusLog.REFUND_STATUS;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Sauravi Thapa ON 5/25/20
 */

@Service
@Transactional
@Slf4j
public class RefundStatusServiceImpl implements RefundStatusService {

    private final AppointmentRefundDetailRepository refundDetailRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final IntegrationCheckPointService integrationCheckpointService;

    public RefundStatusServiceImpl(AppointmentRefundDetailRepository refundDetailRepository,
                                   AppointmentRepository appointmentRepository,
                                   AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                   IntegrationCheckPointService integrationCheckpointService) {
        this.refundDetailRepository = refundDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.integrationCheckpointService = integrationCheckpointService;
    }

    @Override
    public RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO,
                                                            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, REFUND_STATUS);

        RefundStatusResponseDTO response = refundDetailRepository.searchRefundAppointments(requestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public void checkRefundStatus(RefundStatusRequestDTO requestDTO)
            throws IOException {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, REFUND_STATUS);

        AppointmentRefundDetail appointmentRefundDetail = getAppointmentRefundDetail(requestDTO);

        if (appointmentRefundDetail.getStatus().equals(REJECTED)) {
            throw new BadRequestException(APPOINTMENT_HAS_BEEN_REJECTED);
        }

        Appointment appointment = getAppointment(requestDTO);

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointment.getId());

        integrationCheckpointService.apiIntegrationCheckpointForRefundStatus(appointment,
                appointmentRefundDetail,
                appointmentTransactionDetail,
                requestDTO);


        log.info(SAVING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, REFUND_STATUS);

        AppointmentRefundDetailResponseDTO refundAppointments = refundDetailRepository.fetchRefundDetailsById(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    /* Requests esewa api to check the cancelled appointment's status in their side
    * if Response returns COMPLETED our db should maintain 'A' status in Refund table
     * and 'RE' in Appointment table*/
//    private ThirdPartyResponse checkEsewaRefundStatus(RefundStatusRequestDTO requestDTO,
//                                                      Appointment appointment,
//                                                      IntegrationRefundRequestDTO refundRequestDTO) throws IOException {
//
//        IntegrationBackendRequestDTO backendRequestDTO = IntegrationBackendRequestDTO.builder()
//                .integrationChannelCode(refundRequestDTO.getIntegrationChannelCode())
//                .featureCode(refundRequestDTO.getFeatureCode())
//                .appointmentId(refundRequestDTO.getAppointmentId())
//                .build();
//
//        EsewaPaymentStatus esewaPayementStatus = parseToEsewaPaymentStatus(requestDTO);
//
//        String generatedEsewaHmac = getSignatureForEsewa.apply(esewaPayementStatus.getEsewa_id(),
//                esewaPayementStatus.getProduct_code());
//
//        BackendIntegrationApiInfo integrationApiInfo = integrationCheckpointService.
//                getAppointmentModeApiIntegration(backendRequestDTO,
//                        generatedEsewaHmac);
//
//
////        Map<String, Object> esewaRefundRequestDTO = RequestBodyUtils.getDynamicEsewaRequestBodyLog(
////                integrationApiInfo.getRequestBody());
//
////        parseApiUri(integrationApiInfo.getApiUri(), requestDTO.getTransactionNumber());
//
//
//        ResponseEntity<?> response = thirdPartyConnectorService.callEsewaRefundStatusService(integrationApiInfo,
//                esewaPayementStatus);
//
//        ThirdPartyResponse thirdPartyResponse = null;
//        try {
//            thirdPartyResponse = map(response.getBody().toString(),
//                    ThirdPartyResponse.class);
//        } catch (IOException e)
//
//        {
//            e.printStackTrace();
//            throw new OperationUnsuccessfulException("ThirdParty API response is null");
//        }
//
//        if (thirdPartyResponse.getCode() != null) {
////            validateThirdPartyException(thirdPartyResponse);
//        }
//
//
//        return thirdPartyResponse;
//    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentRefundDetail getAppointmentRefundDetail(RefundStatusRequestDTO requestDTO) {
        return refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);
    }

    private Appointment getAppointment(RefundStatusRequestDTO requestDTO) {
        return appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_TRANSACTION_DETAIL, appointmentId);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "appointmentId", appointmentId.toString());
    };
}
