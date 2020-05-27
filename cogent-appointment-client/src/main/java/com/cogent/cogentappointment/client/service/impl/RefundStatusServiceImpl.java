package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.EsewaResponseDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import com.cogent.cogentappointment.client.utils.resttempalte.RestTemplateUtils;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_FONEPAY_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.RefundStatusResponseConstant.FULL_REFUND;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.RefundStatusResponseConstant.PARTIAL_REFUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.RefundStatusLog.REFUND_STATUS;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestHeaders.getEsewaPaymentStatusAPIHeaders;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestURI.ESEWA_API_PAYMENT_STATUS;

/**
 * @author Sauravi Thapa ON 5/25/20
 */

@Service
@Transactional
@Slf4j
public class RefundStatusServiceImpl implements RefundStatusService {

    private final AppointmentRefundDetailRepository refundDetailRepository;

    private final AppointmentRepository appointmentRepository;

    private final RestTemplateUtils restTemplateUtils;


    public RefundStatusServiceImpl(AppointmentRefundDetailRepository refundDetailRepository,
                                   AppointmentRepository appointmentRepository,
                                   RestTemplateUtils restTemplateUtils) {
        this.refundDetailRepository = refundDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.restTemplateUtils = restTemplateUtils;
    }

    @Override
    public RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, REFUND_STATUS);

        RefundStatusResponseDTO response = refundDetailRepository.searchRefundAppointments(requestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public void checkRefundStatus(RefundStatusRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, REFUND_STATUS);

        String response = processRefundRequest(requestDTO);

        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(requestDTO);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(requestDTO);
                break;

        }


        log.info(SAVING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));
    }

    /* Requests esewa api to check the cancelled appointment's status in their side
    * if Response returns COMPLETED our db should maintain 'A' status in Refund table
     * and 'RE' in Appointment table*/
    private String checkEsewaRefundStatus(RefundStatusRequestDTO requestDTO) {

        EsewaPayementStatus esewaPayementStatus = parseToEsewaPayementStatus(requestDTO);

        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, getEsewaPaymentStatusAPIHeaders());

        ResponseEntity<EsewaResponseDTO> response = (ResponseEntity<EsewaResponseDTO>) restTemplateUtils.
                postRequest(ESEWA_API_PAYMENT_STATUS,
                        request, EsewaResponseDTO.class);

        return response.getBody().getStatus();
    }

    private String processRefundRequest(RefundStatusRequestDTO requestDTO) {

        switch (requestDTO.getAppointmentMode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:
                return checkEsewaRefundStatus(requestDTO);

            case APPOINTMENT_MODE_FONEPAY_CODE:
                break;

            default:
                break;
        }


        return null;
    }

    private void changeAppointmentAndAppointmentRefundDetailStatus(RefundStatusRequestDTO requestDTO) {

        AppointmentRefundDetail appointmentRefundDetail = refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);

        Appointment appointment = appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);

        saveAppointment(changeAppointmentStatus.apply(appointment));

        saveAppointmentRefundDetails(changeAppointmentRefundDetailStatus.apply(appointmentRefundDetail));
    }

    private void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetails(AppointmentRefundDetail appointmentRefundDetail) {
        refundDetailRepository.save(appointmentRefundDetail);
    }
}
