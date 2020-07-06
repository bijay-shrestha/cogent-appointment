package com.cogent.cogentappointment.esewa.scheduler;

import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentReservationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author smriti on 18/02/20
 */
@Configuration
@EnableScheduling
@Slf4j
public class AppointmentHospitalDepartmentReservationScheduler {

    private final AppointmentHospitalDepartmentReservationLogService appointmentHospitalDepartmentReservationLogService;

    public AppointmentHospitalDepartmentReservationScheduler(
            AppointmentHospitalDepartmentReservationLogService appointmentHospitalDepartmentReservationLogService) {
        this.appointmentHospitalDepartmentReservationLogService = appointmentHospitalDepartmentReservationLogService;
    }

//    /*RUNS IN 2 MINS*/
//    @Scheduled(fixedDelayString = APPOINTMENT_RESERVATION_SCHEDULER)
//    public void deleteExpiredAppointmentReservation() {
//        log.info(SCHEDULER_RUNNING, APPOINTMENT_RESERVATION_LOG);
//        appointmentHospitalDepartmentReservationLogService.deleteExpiredAppointmentHospitalDeptReservation();
//    }
}
