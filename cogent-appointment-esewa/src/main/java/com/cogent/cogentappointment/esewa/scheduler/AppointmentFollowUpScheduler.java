package com.cogent.cogentappointment.esewa.scheduler;

//import com.cogent.cogentappointment.esewa.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.esewa.service.AppointmentFollowUpTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.SCHEDULER_RUNNING;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 20/11/2019
 */
@Configuration
@EnableScheduling
@Slf4j
public class AppointmentFollowUpScheduler {

    private final AppointmentFollowUpTrackerService appointmentFollowUpTrackerService;

    public AppointmentFollowUpScheduler(AppointmentFollowUpTrackerService appointmentFollowUpTrackerService) {
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
    }

    @Scheduled(fixedDelayString = "${followup.schedulerTime}")
    public void updateFollowUpTracker() {
        log.info(SCHEDULER_RUNNING, APPOINTMENT_FOLLOW_UP_TRACKER);
        appointmentFollowUpTrackerService.updateFollowUpTrackerStatus();
    }
}
