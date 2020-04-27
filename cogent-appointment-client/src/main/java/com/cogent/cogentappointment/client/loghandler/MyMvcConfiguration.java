package com.cogent.cogentappointment.client.loghandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Rupak
 */
@Configuration
public class MyMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.myLogInterceptor());
    }

    @Bean
    public UserLogInterceptor myLogInterceptor() {
        return new UserLogInterceptor();
    }
}