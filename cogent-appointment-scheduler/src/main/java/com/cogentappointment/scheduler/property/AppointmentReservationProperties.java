package com.cogentappointment.scheduler.property;

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
@ConfigurationProperties(prefix = "reservation")
public class AppointmentReservationProperties {

    private String schedulerTime;

    private String deleteTime;
}
