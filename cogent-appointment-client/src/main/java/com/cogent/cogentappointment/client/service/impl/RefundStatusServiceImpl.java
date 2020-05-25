package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.service.RefundStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public RefundStatusServiceImpl(AppointmentRefundDetailRepository refundDetailRepository,
                                   AppointmentRepository appointmentRepository) {
        this.refundDetailRepository = refundDetailRepository;
        this.appointmentRepository = appointmentRepository;
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
    public void approveAppointmentRefund(RefundStatusRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, REFUND_STATUS);

        AppointmentRefundDetail appointmentRefundDetail=refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);

        Appointment appointment=appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);

        saveAppointment(changeAppointmentStatus(appointment,requestDTO));

        saveAppointmentRefundDetails(changeAppointmentRefundDetailStatus(appointmentRefundDetail,requestDTO));

        log.info(SAVING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveAppointment( Appointment appointment){
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetails(AppointmentRefundDetail appointmentRefundDetail){
        refundDetailRepository.save(appointmentRefundDetail);
    }
}
