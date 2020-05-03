package com.cogent.cogentappointment.scheduler;

import com.cogent.cogentappointment.persistence.util.BeanUtil;
import com.cogent.cogentappointment.scheduler.configuration.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.cogent.cogentappointment.persistence.model"})
@EnableJpaRepositories
@PropertySource(
        factory = YamlPropertySourceFactory.class,
        value =
                {
                        "file:${catalina.home}/conf/scheduler/application-${spring.profiles.active}.yml"
                })
public class CogentAppointmentSchedulerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CogentAppointmentSchedulerApplication.class);
    }

    public static void main(String[] args) {
//        SpringApplication.run(CogentAppointmentSchedulerApplication.class, args);
        ConfigurableApplicationContext ctx =
                SpringApplication.run(CogentAppointmentSchedulerApplication.class, args);
    }

    @Bean
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }
}
