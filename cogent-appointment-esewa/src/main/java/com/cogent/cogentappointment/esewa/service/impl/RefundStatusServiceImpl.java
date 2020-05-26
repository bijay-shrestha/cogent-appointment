package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.esewa.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.esewa.repository.AppointmentRepository;
import com.cogent.cogentappointment.esewa.service.RefundStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.RefundStatusLog.REFUND_STATUS;
import static com.cogent.cogentappointment.esewa.utils.RefundStatusUtils.changeAppointmentRefundDetailStatus;
import static com.cogent.cogentappointment.esewa.utils.RefundStatusUtils.changeAppointmentStatus;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
    public void approveAppointmentRefund(RefundStatusRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, REFUND_STATUS);

        AppointmentRefundDetail appointmentRefundDetail = refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);

        Appointment appointment = appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);

        saveAppointment(changeAppointmentStatus(appointment));

        saveAppointmentRefundDetails(changeAppointmentRefundDetailStatus(appointmentRefundDetail));

        log.info(SAVING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetails(AppointmentRefundDetail appointmentRefundDetail) {
        refundDetailRepository.save(appointmentRefundDetail);
    }
}
