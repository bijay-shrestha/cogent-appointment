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

    @Value("${minio.url}")
    private String URL;

    @Value("${minio.bucket}")
    private String BUCKET_NAME;

    @Value("${minio.access-key}")
    private String ACCESS_KEY;

    @Value("${minio.secret-key}")
    private String SECRET_KEY;

    @Value("${minio.expiry-time}")
    private String EXPIRY_TIME;


}
