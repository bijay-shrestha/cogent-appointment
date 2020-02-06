package com.cogent.cogentappointment.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CogentAppointmentPersistenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CogentAppointmentPersistenceApplication.class, args);
    }

}
