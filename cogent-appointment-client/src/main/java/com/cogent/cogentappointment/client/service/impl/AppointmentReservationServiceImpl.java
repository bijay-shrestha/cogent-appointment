package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.repository.AppointmentReservationLogRepository;
import com.cogent.cogentappointment.client.service.AppointmentReservationService;
import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentReservationLogConstant.APPOINTMENT_RESERVATION_LOG;
import static com.cogent.cogentappointment.client.utils.AppointmentReservationLogUtils.parseToAppointmentReservation;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

        Long appointmentReservationLogId = fetchAppointmentReservationLogId(requestDTO);

        if (Objects.isNull(appointmentReservationLogId)) {
            AppointmentReservationLog appointmentReservation = parseToAppointmentReservation(requestDTO);
            appointmentReservationLogId = save(appointmentReservation);
        }

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION_LOG, getDifferenceBetweenTwoTime(startTime));

        return appointmentReservationLogId;
    }

    private Long save(AppointmentReservationLog appointmentReservation) {
        AppointmentReservationLog reservationLog = appointmentReservationLogRepository.save(appointmentReservation);
        return reservationLog.getId();
    }

    private Long fetchAppointmentReservationLogId(AppointmentFollowUpRequestDTO requestDTO) {
        return appointmentReservationLogRepository.fetchAppointmentReservationLogId(
                requestDTO.getAppointmentDate(),
                requestDTO.getAppointmentTime(),
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId()
        );
    }

}
