package com.cogent.cogentappointment.client.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author smriti on 7/20/19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "forgotpassword")
public class ForgotPasswordProperties implements Serializable{

    private int expiryTime;
}
