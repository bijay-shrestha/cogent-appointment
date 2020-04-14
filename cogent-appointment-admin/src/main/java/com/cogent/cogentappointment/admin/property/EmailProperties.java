package com.cogent.cogentappointment.admin.property;

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
@ConfigurationProperties(prefix = "mail")
public class EmailProperties implements Serializable {

    private String host;

    private String username;

    private String password;

    private int port;

    private String protocol;
}
