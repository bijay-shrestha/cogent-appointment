package com.cogent.cogentappointment.client.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author smriti on 14/02/20
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "followup")
public class AppointmentFollowUpProperties {

    private String schedulerTime;
}
