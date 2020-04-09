package com.cogent.cogentappointment.admin.configuration;

import com.cogent.cogentappointment.admin.service.AdminLogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Rupak
 */
@Configuration
public class MyMvcConfiguration extends WebMvcConfigurerAdapter {

    private final AdminLogService adminLogService;

    public MyMvcConfiguration(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.myLogInterceptor());
    }
    @Bean
    public MyLogInterceptor myLogInterceptor() {
        return new MyLogInterceptor(adminLogService);
    }
}