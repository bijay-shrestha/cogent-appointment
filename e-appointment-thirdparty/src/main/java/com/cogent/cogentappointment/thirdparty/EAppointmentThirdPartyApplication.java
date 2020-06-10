package com.cogent.cogentappointment.thirdparty;

import com.cogent.cogentappointment.persistence.util.BeanUtil;
import com.cogent.cogentappointment.thirdparty.configuration.YamlPropertySourceFactory;
import org.modelmapper.ModelMapper;
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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan(basePackages = {"com.cogent.cogentappointment.persistence.model",
        "com.cogent.cogentappointment.persistence.history"})

@EnableJpaRepositories
@PropertySource(
        factory = YamlPropertySourceFactory.class,
        value =
                {
                        "file:${catalina.home}/conf/third-party/application-${spring.profiles.active}.yml"
                })
public class EAppointmentThirdPartyApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources( EAppointmentThirdPartyApplication.class);
    }

    //  BEFORE RUNNING THIS CHECK IF 'catalina.home' 
    // PATH IS SET IN YOUR 'application.yml'
    public static void main(String[] args) {
        
        ConfigurableApplicationContext ctx =
                SpringApplication.run( EAppointmentThirdPartyApplication.class, args);

        ConfigurableEnvironment env = ctx.getEnvironment();
        env.getPropertySources()
                .forEach(ps -> System.out.println(ps.getName() + " : " + ps.getClass()));
        System.out.println("Value of `spring.profiles.active` = " + env.getProperty("spring.profiles.active"));
        System.out.println("Value of `application-${spring.profiles.active}.yml`");
        System.out.println("Value of `spring.datasource.url` = " + env.getProperty("spring.datasource.url"));
        System.out.println("Value of `server.port` = " + env.getProperty("server.port"));
        System.out.println("Value of `catalina.home` = " + env.getProperty("catalina.home"));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }

}

    
