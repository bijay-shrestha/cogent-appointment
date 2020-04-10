package com.cogent.cogentappointment.client.configuration;

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
}
