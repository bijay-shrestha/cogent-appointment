package com.cogent.cogentthirdpartyconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.cogent.cogentappointment.persistence.model",
        "com.cogent.cogentappointment.persistence.history"})
@EnableJpaRepositories
public class CogentThirdpartyConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CogentThirdpartyConnectorApplication.class, args);
    }

}
