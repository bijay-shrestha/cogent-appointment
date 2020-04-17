package com.cogent.cogentappointment.admin.scheduler;

import com.cogent.cogentappointment.admin.service.AppointmentFollowUpTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.cogent.cogentappointment.admin.constants.SchedulerConstants.FOLLOW_UP_SCHEDULER_TIME;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SCHEDULER_RUNNING;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;

/**
 * @author smriti on 20/11/2019
 */
@Configuration
@EnableScheduling
@Slf4j
public class EmailScheduler {

    private final AppointmentFollowUpTrackerService appointmentFollowUpTrackerService;

    public EmailScheduler(AppointmentFollowUpTrackerService appointmentFollowUpTrackerService) {
        this.appointmentFollowUpTrackerService = appointmentFollowUpTrackerService;
    }

    /*SCHEDULER RUNS EACH DAY AND FOR EACH ACTIVE FOLLOW UP APPOINTMENTS,
   SCHEDULER UPDATES STATUS AS INACTIVE IF HAS EXPIRED(DEPENDS ON CLIENT/HOSPITAL FOLLOW UP INTERVAL DAYS */
    @Scheduled(fixedDelayString = FOLLOW_UP_SCHEDULER_TIME)
    public void updateFollowUpTracker() {
        log.info(SCHEDULER_RUNNING, APPOINTMENT_FOLLOW_UP_TRACKER);
        appointmentFollowUpTrackerService.updateFollowUpTrackerStatus();
    }
}
