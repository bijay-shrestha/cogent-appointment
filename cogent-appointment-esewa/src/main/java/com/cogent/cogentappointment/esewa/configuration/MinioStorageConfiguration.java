package com.cogent.cogentappointment.esewa.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rupak
 */
@Configuration
@Getter
public class MinioStorageConfiguration {

    @Value("${serverlocation}")
    private String serverlocation;

    @Value("${spring.minio.bucket}")
    private String bucket;

    @Value("${spring.minio.secret-key}")
    private String secretKey;
}
