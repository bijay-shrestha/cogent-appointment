package com.cogent.cogentappointment.commons.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/28-1:08 PM
 */
@Configuration
@ConfigurationProperties(
        ignoreUnknownFields = true,
        ignoreInvalidFields = true)
@Getter
public class MinIOProperties implements Serializable {

    @Value("${spring.minio.url}")
    private String URL;

    @Value("${cdn-url}")
    private String CDN_URL;

    @Value("${spring.minio.bucket}")
    private String BUCKET_NAME;

    @Value("${spring.minio.access-key}")
    private String ACCESS_KEY;

    @Value("${spring.minio.secret-key}")
    private String SECRET_KEY;

    @Value("${spring.minio.expiry-time}")
    private String EXPIRY_TIME;


}
