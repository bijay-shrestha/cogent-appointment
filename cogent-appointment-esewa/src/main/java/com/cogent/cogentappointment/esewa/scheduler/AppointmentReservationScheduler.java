package com.cogent.cogentappointment.esewa.scheduler;

import com.cogent.cogentappointment.esewa.service.AppointmentReservationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.cogent.cogentappointment.esewa.constants.SchedulerConstants.APPOINTMENT_RESERVATION_SCHEDULER;
import static com.cogent.cogentappointment.esewa.constants.WebResourceKeyConstants.SCHEDULER_RUNNING;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentReservationLog.APPOINTMENT_RESERVATION_LOG;

/**
 * @author smriti on 18/02/20
 */
@Configuration
@EnableScheduling
@Slf4j
public class AppointmentReservationScheduler {

    private final AppointmentReservationLogService appointmentReservationLogService;

    public AppointmentReservationScheduler(AppointmentReservationLogService appointmentReservationLogService) {
        this.appointmentReservationLogService = appointmentReservationLogService;
    }

//    /*RUNS IN 2 MINS*/
    @Scheduled(fixedDelayString = APPOINTMENT_RESERVATION_SCHEDULER)
    public void deleteExpiredAppointmentReservation() {
        log.info(SCHEDULER_RUNNING, APPOINTMENT_RESERVATION_LOG);
        appointmentReservationLogService.deleteExpiredAppointmentReservation();
    }
}
