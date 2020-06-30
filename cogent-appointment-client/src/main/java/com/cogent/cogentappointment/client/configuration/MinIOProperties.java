package com.cogent.cogentappointment.client.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author rupak ON 2020/06/28-1:08 PM
 */
@Configuration
@Getter
public class MinIOProperties {

    @Value("${spring.minio.url}")
    private String URL;

    @Value("${spring.minio.bucket}")
    private String BUCKET_NAME;

    @Value("${spring.minio.access-key}")
    private String ACCESS_KEY;

    @Value("${spring.minio.secret-key}")
    private String SECRET_KEY;

    @Value("${spring.minio.expiry-time}")
    private String EXPIRY_TIME;


}
