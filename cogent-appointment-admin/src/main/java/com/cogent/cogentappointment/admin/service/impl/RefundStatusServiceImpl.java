package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.admin.service.RefundStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentthirdpartyconnector.request.EsewaPayementStatus;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.RefundStatusLog.REFUND_STATUS;
import static com.cogent.cogentappointment.admin.security.hmac.HMACUtils.getSignatureForEsewa;
import static com.cogent.cogentappointment.admin.utils.RefundStatusUtils.*;
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
    private final AppointmentService appointmentService;
    private final ThirdPartyConnectorService thirdPartyConnectorService;
    private final IntegrationThirdPartyImpl integrationThirdPartyImpl;

    public RefundStatusServiceImpl(AppointmentRefundDetailRepository refundDetailRepository,
                                   AppointmentRepository
                                           appointmentRepository,
                                   AppointmentService appointmentService,
                                   ThirdPartyConnectorService thirdPartyConnectorService,
                                   IntegrationThirdPartyImpl integrationThirdPartyImpl) {
        this.refundDetailRepository = refundDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.integrationThirdPartyImpl = integrationThirdPartyImpl;
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
    public void checkRefundStatus(RefundStatusRequestDTO requestDTO,
                                  IntegrationBackendRequestDTO integrationBackendRequestDTO)
            throws IOException {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, REFUND_STATUS);

        AppointmentRefundDetail appointmentRefundDetail = getAppointmentRefundDetail(requestDTO);

        Appointment appointment = getAppointment(requestDTO);

        requestDTO.setEsewaMerchantCode("testBir");

        requestDTO.setAppointmentMode(appointment.getAppointmentModeId().getCode());

        ThirdPartyResponse response = processRefundRequest(requestDTO,
                appointment,
                integrationBackendRequestDTO);

        if (!Objects.isNull(response.getCode())) {
            throw new BadRequestException(response.getMessage(), response.getMessage());
        }

        switch (response.getStatus()) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        response.getStatus());
                break;

            case COMPLETE:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        response.getStatus());
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment,
                        appointmentRefundDetail,
                        response.getStatus());
                break;

            case AMBIGUOUS:
                appointmentService.approveRefundAppointment(requestDTO.getAppointmentId(),
                        requestDTO.getIntegrationBackendRequestDTO());

            default:
                appointmentService.approveRefundAppointment(requestDTO.getAppointmentId(),
                        requestDTO.getIntegrationBackendRequestDTO());

        }

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
    private ThirdPartyResponse checkEsewaRefundStatus(RefundStatusRequestDTO requestDTO,
                                                      Appointment appointment,
                                                      IntegrationBackendRequestDTO backendRequestDTO) throws IOException {


        EsewaPayementStatus esewaPayementStatus = parseToEsewaPaymentStatus(requestDTO);

        String generatedEsewaHmac = getSignatureForEsewa.apply(esewaPayementStatus.getEsewa_id(),
                esewaPayementStatus.getProduct_code());

        BackendIntegrationApiInfo integrationApiInfo = integrationThirdPartyImpl.
                getAppointmentModeApiIntegration(backendRequestDTO,
                        appointment.getAppointmentModeId().getId(), generatedEsewaHmac);


//        Map<String, Object> esewaRefundRequestDTO = RequestBodyUtils.getDynamicEsewaRequestBodyLog(
//                integrationApiInfo.getRequestBody());

//        parseApiUri(integrationApiInfo.getApiUri(), requestDTO.getTransactionNumber());


        ThirdPartyResponse response = thirdPartyConnectorService.callEsewaRefundStatusService(integrationApiInfo,
                esewaPayementStatus);


        return response;
    }

    private ThirdPartyResponse processRefundRequest(RefundStatusRequestDTO requestDTO,
                                                    Appointment appointment,
                                                    IntegrationBackendRequestDTO integrationBackendRequestDTO) throws IOException {

        ThirdPartyResponse thirdPartyResponse = null;

        switch (requestDTO.getAppointmentMode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:
                thirdPartyResponse = checkEsewaRefundStatus(requestDTO,
                        appointment,
                        integrationBackendRequestDTO);
                break;

            default:
                throw new BadRequestException("APPOINTMENT MODE NOT VALID");
        }

        return thirdPartyResponse;
    }

    private void changeAppointmentAndAppointmentRefundDetailStatus(Appointment appointment,
                                                                   AppointmentRefundDetail appointmentRefundDetail,
                                                                   String remarks) {

        saveAppointment(changeAppointmentStatus.apply(appointment, remarks));

        saveAppointmentRefundDetails(changeAppointmentRefundDetailStatus.apply(appointmentRefundDetail, remarks));
    }

    private AppointmentRefundDetail getAppointmentRefundDetail(RefundStatusRequestDTO requestDTO) {
        return refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);
    }

    private Appointment getAppointment(RefundStatusRequestDTO requestDTO) {
        return appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);
    }


    private void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetails(AppointmentRefundDetail appointmentRefundDetail) {
        refundDetailRepository.save(appointmentRefundDetail);
    }
}
