package com.cogent.cogentappointment.admin.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author smriti on 7/20/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "time")
public class ExpirationTimeProperties {

    private int forgotPassword;
}
