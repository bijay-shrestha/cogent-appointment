package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.AppointmentService;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentModeConstant.APPOINTMENT_MODE_ESEWA_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.RefundStatusLog.REFUND_STATUS;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.changeAppointmentRefundDetailStatus;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.changeAppointmentStatus;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

    public RefundStatusServiceImpl(AppointmentRefundDetailRepository refundDetailRepository,
                                   AppointmentRepository appointmentRepository,
                                   AppointmentService appointmentService) {
        this.refundDetailRepository = refundDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
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

        AppointmentRefundDetail appointmentRefundDetail = getAppointmentRefundDetail(requestDTO);

        Appointment appointment = getAppointment(requestDTO);

        requestDTO.setEsewaMerchantCode(appointment.getHospitalId().getEsewaMerchantCode());

        requestDTO.setAppointmentMode(appointment.getAppointmentModeId().getCode());

        String response = processRefundRequest(requestDTO);

        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment, appointmentRefundDetail, response);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment, appointmentRefundDetail, response);
                break;

            case AMBIGIOUS :
                appointmentService.approveRefundAppointment(requestDTO.getIntegrationBackendRequestDTO());

            default:
                appointmentService.approveRefundAppointment(requestDTO.getIntegrationBackendRequestDTO());
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
    private String checkEsewaRefundStatus(RefundStatusRequestDTO requestDTO) {

//        EsewaPayementStatus esewaPayementStatus = parseToEsewaPayementStatus(requestDTO);
//
//        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, getEsewaPaymentStatusAPIHeaders());

//        ResponseEntity<EsewaResponseDTO> response = (ResponseEntity<EsewaResponseDTO>) restTemplateUtils.
//                postRequest(ESEWA_API_PAYMENT_STATUS,
//                        request, EsewaResponseDTO.class);

//        return (response.getBody().getStatus() == null) ? AMBIGIOUS : response.getBody().getStatus();

        return null;
    }

    private String processRefundRequest(RefundStatusRequestDTO requestDTO) {

        String thirdPartyResponse = null;

        switch (requestDTO.getAppointmentMode()) {

            case APPOINTMENT_MODE_ESEWA_CODE:
                thirdPartyResponse = checkEsewaRefundStatus(requestDTO);
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
