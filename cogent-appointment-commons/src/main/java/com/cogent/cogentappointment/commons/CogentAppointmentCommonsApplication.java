package com.cogent.cogentappointment.commons;

import com.cogent.cogentappointment.commons.configuration.YamlPropertySourceFactory;
import com.cogent.cogentappointment.persistence.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages =
        {"com.cogent.cogentappointment.persistence.model",
                "com.cogent.cogentappointment.persistence.history"})
@PropertySource(
        factory = YamlPropertySourceFactory.class,
        value =
                {
                        "file:${catalina.home}/conf/commons/application-${spring.profiles.active}.yml"
                })
public class CogentAppointmentCommonsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CogentAppointmentCommonsApplication.class);
    }


    public static void main(String[] args) {

//        SpringApplication.run(CogentAppointmentAdminApplication.class, args);
        ConfigurableApplicationContext ctx =
                SpringApplication.run(CogentAppointmentCommonsApplication.class, args);

        ConfigurableEnvironment env = ctx.getEnvironment();
        env.getPropertySources()
                .forEach(ps -> System.out.println(ps.getName() + " : " + ps.getClass()));
        System.out.println("Value of `spring.profiles.active` = " + env.getProperty("spring.profiles.active"));
        System.out.println("Value of `application-${spring.profiles.active}.yml`");
        System.out.println("Value of `spring.datasource.url` = " + env.getProperty("spring.datasource.url"));
        System.out.println("Value of `spring.minio.url` = " + env.getProperty("spring.minio.url"));
        System.out.println("Value of `spring.minio.bucket` = " + env.getProperty("spring.minio.bucket"));
        System.out.println("Value of `serverlocation` = " + env.getProperty("serverlocation"));
        System.out.println("Value of `spring.minio.access-key` = " + env.getProperty("spring.minio.access-key"));
        System.out.println("Value of `mail.host` = " + env.getProperty("mail.host"));
        System.out.println("Value of `catalina.home` = " + env.getProperty("catalina.home"));
    }

//    @Bean
//    public BeanUtil beanUtil() {
//        return new BeanUtil();
//    }


}