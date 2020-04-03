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

    @Value("${serverlocation}")
    private String serverlocation;
}
