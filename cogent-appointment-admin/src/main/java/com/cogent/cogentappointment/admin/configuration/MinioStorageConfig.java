package com.cogent.cogentappointment.admin.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rupak
 */
@Configuration
@Getter
public class MinioStorageConfig {

    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.bucket}")
    private String bucket;

    @Value("${spring.minio.secret-key}")
    private String secretKey;
}
