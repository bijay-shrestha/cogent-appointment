package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.repository.AppointmentReservationLogRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentReservationService;
import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentReservationLog.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.esewa.utils.AppointmentReservationLogUtils.parseToAppointmentReservation;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 18/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentReservationServiceImpl implements AppointmentReservationService {

    private final AppointmentReservationLogRepository appointmentReservationLogRepository;

    public AppointmentReservationServiceImpl(AppointmentReservationLogRepository appointmentReservationLogRepository) {
        this.appointmentReservationLogRepository = appointmentReservationLogRepository;
    }

    @Override
    public Long save(AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_RESERVATION_LOG);

        AppointmentReservationLog appointmentReservation =
                parseToAppointmentReservation(requestDTO);

        Long savedId = save(appointmentReservation);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));

        return savedId;
    }

    private Long save(AppointmentReservationLog appointmentReservation) {
        AppointmentReservationLog reservationLog = appointmentReservationLogRepository.save(appointmentReservation);
        return reservationLog.getId();
    }

}
