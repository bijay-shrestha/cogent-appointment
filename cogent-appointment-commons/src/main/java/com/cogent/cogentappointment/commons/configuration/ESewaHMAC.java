package com.cogent.cogentappointment.commons.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author rupak ON 2020/07/03-2:55 PM
 */
@Configuration
@ConfigurationProperties(
        ignoreUnknownFields = true,
        ignoreInvalidFields = true)
@Getter
public class ESewaHMAC {

    @Value("${esewa-hmac-alogrithm}")
    public String HMAC_ALGORITHM;

    @Value("${esewa-hmac-api-secret}")
    public String HMAC_API_SECRET_ESEWA;

}
