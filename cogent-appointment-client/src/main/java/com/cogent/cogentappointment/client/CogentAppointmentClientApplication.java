package com.cogent.cogentappointment.client;

import com.cogent.cogentappointment.persistence.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.cogent.cogentappointment.persistence.model",
        "com.cogent.cogentappointment.persistence.history"})
@EnableJpaRepositories
public class CogentAppointmentClientApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CogentAppointmentClientApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CogentAppointmentClientApplication.class, args);
    }

    @Bean
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }
}
