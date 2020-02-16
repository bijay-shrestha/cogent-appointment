package com.cogent.cogentappointment.admin;

import com.cogent.cogentappointment.persistence.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.cogent.cogentappointment.persistence.model",
        "com.cogent.cogentappointment.persistence.history"})
@EnableJpaRepositories
public class CogentAppointmentAdminApplication {

    public static void main(String[] args) {

        SpringApplication.run(CogentAppointmentAdminApplication.class, args);
    }

    @Bean
    public BeanUtil beanUtil(){
        return new BeanUtil();
    }

}
