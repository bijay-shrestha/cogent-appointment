package com.cogent.cogentappointment.admin.scheduler;

import com.cogent.cogentappointment.admin.property.EmailProperties;
import com.cogent.cogentappointment.admin.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.cogent.cogentappointment.admin.constants.SchedulerConstants.EMAIL_SCHEDULER_TIME;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SCHEDULER_RUNNING;
import static com.cogent.cogentappointment.admin.log.constants.EmailLog.EMAIL;

/**
 * @author smriti on 20/11/2019
 */
@Configuration
@EnableScheduling
@Slf4j
public class EmailScheduler {

    private final EmailService emailService;

    private final EmailProperties emailProperties;

    public EmailScheduler(EmailService emailService, EmailProperties emailProperties) {
        this.emailService = emailService;
        this.emailProperties = emailProperties;
    }

    /*SCHEDULER RUNS EVERY MINUTE DAY AND SENDS EMAILS FOR EACH UNSENT ONES */
    @Scheduled(fixedDelayString = EMAIL_SCHEDULER_TIME)
    public void sendEmail() {
        if (emailProperties.getEnabled().equals(YES)) {
            log.info(SCHEDULER_RUNNING, EMAIL);
            emailService.sendEmail();
        }
    }
}
