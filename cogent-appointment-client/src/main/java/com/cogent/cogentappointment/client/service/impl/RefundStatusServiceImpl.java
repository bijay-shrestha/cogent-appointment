package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import com.cogent.cogentappointment.client.utils.resttempalte.RestTemplateUtils;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.RefundStatusLog.REFUND_STATUS;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.changeAppointmentRefundDetailStatus;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.changeAppointmentStatus;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.parseToEsewaPayementStatus;
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
                                   AppointmentRepository appointmentRepository, RestTemplateUtils restTemplateUtils) {
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

        String response=requestEsewaApi(requestDTO);

        if(response.toString().equals("COMPLETED")) {

            AppointmentRefundDetail appointmentRefundDetail = refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);

            Appointment appointment = appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);

            saveAppointment(changeAppointmentStatus(appointment));

            saveAppointmentRefundDetails(changeAppointmentRefundDetailStatus(appointmentRefundDetail));
        }

        log.info(SAVING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));
    }

    /* Requests esewa api to check the cancelled appointment's status in their side
    * if Response returns COMPLETED our db should maintain 'A' status in Refund table
     * and 'RE' in Appointment table*/
    private String requestEsewaApi(RefundStatusRequestDTO requestDTO){
        EsewaPayementStatus esewaPayementStatus=parseToEsewaPayementStatus(requestDTO);

        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, getEsewaPaymentStatusAPIHeaders());

        ResponseEntity<?> response = restTemplateUtils.
                postRequest(ESEWA_API_PAYMENT_STATUS,
                        new HttpEntity<>(request, getEsewaPaymentStatusAPIHeaders()));

        String objects= String.valueOf(response.getBody());

        return null;
    }

    private void saveAppointment( Appointment appointment){
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetails(AppointmentRefundDetail appointmentRefundDetail){
        refundDetailRepository.save(appointmentRefundDetail);
    }
}
