package com.cogent.cogentappointment.scheduler.service.impl;

import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;
import com.cogent.cogentappointment.scheduler.property.AppointmentReservationProperties;
import com.cogent.cogentappointment.scheduler.repository.AppointmentReservationRepository;
import com.cogent.cogentappointment.scheduler.service.AppointmentReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cogent.cogentappointment.scheduler.log.AppointmentReservation.APPOINTMENT_RESERVATION;
import static com.cogent.cogentappointment.scheduler.log.CommonLogConstant.DELETING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.scheduler.log.CommonLogConstant.DELETING_PROCESS_STARTED;
import static com.cogent.cogentappointment.scheduler.utils.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.scheduler.utils.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 18/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentReservationServiceImpl implements AppointmentReservationService {

    private final AppointmentReservationRepository appointmentReservationRepository;

    private final AppointmentReservationProperties reservationProperties;

    public AppointmentReservationServiceImpl(AppointmentReservationRepository appointmentReservationRepository,
                                             AppointmentReservationProperties reservationProperties) {
        this.appointmentReservationRepository = appointmentReservationRepository;
        this.reservationProperties = reservationProperties;
    }

    /*SCHEDULER - 2 MINS
    * DELETE - 5 MINS*/
    @Override
    public void deleteExpiredAppointmentReservation() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_RESERVATION);

        List<AppointmentReservationLog> appointmentReservations =
                appointmentReservationRepository.fetchAppointmentReservationLog();

        appointmentReservations.forEach(appointmentReservation -> {

            long expiryDate = appointmentReservation.getCreatedDate().getTime() +
                    TimeUnit.MINUTES.toMillis(Long.parseLong(reservationProperties.getDeleteIntervalInMinutes()));

            long currentDateInMillis = new Date().getTime();

            if (expiryDate < currentDateInMillis)
                appointmentReservationRepository.delete(appointmentReservation);
        });

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_RESERVATION, getDifferenceBetweenTwoTime(startTime));
    }
}
