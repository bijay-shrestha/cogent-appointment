package com.cogent.cogentappointment.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages =
        {"com.cogent.cogentappointment.persistence.model",
                "com.cogent.cogentappointment.persistence.history"})
public class CogentAppointmentCommonsApplication {


    public static void main(String[] args) {
        SpringApplication.run(CogentAppointmentCommonsApplication.class, args);
    }
}