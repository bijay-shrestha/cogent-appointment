package com.cogent.cogentappointment.admin.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author smriti on 14/02/20
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "followup")
public class AppointmentFollowUpProperties implements Serializable {

    private String schedulerTime;
}
