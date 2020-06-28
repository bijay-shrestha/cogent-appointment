package com.cogent.cogentappointment.commons.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author rupak ON 2020/06/28-1:08 PM
 */
@Configuration
@Getter
public class MinIOProperties {

    @Value("${url}")
    private String URL;

    @Value("${bucket-name}")
    private String BUCKET_NAME;

    @Value("${access-key}")
    private String ACCESS_KEY;

    @Value("${secret-key}")
    private String SECRET_KEY;

    @Value("${expiry-time}")
    private String EXPIRY_TIME;


}
