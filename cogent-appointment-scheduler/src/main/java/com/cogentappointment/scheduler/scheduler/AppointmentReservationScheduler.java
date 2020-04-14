package com.cogentappointment.scheduler.scheduler;

import com.cogentappointment.scheduler.service.AppointmentReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.cogentappointment.scheduler.constants.SchedulerConstants.APPOINTMENT_RESERVATION_SCHEDULER;
import static com.cogentappointment.scheduler.log.AppointmentReservation.APPOINTMENT_RESERVATION;
import static com.cogentappointment.scheduler.log.CommonLogConstant.SCHEDULER_RUNNING;

/**
 * @author smriti on 18/02/20
 */
@Configuration
@EnableScheduling
@Slf4j
public class AppointmentReservationScheduler {

    private final AppointmentReservationService appointmentReservationService;

    public AppointmentReservationScheduler(AppointmentReservationService appointmentReservationService) {
        this.appointmentReservationService = appointmentReservationService;
    }

    /*RUNS IN 2 MINS*/
    @Scheduled(fixedDelayString = APPOINTMENT_RESERVATION_SCHEDULER)
    public void deleteExpiredAppointmentReservation() {
        log.info(SCHEDULER_RUNNING, APPOINTMENT_RESERVATION);
        appointmentReservationService.deleteExpiredAppointmentReservation();
    }
}
