package com.cogent.cogentappointment.client.configuration;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.cogent.cogentappointment.client.constants.CacheConstant.*;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                CACHE_REVENUE_STATISTICS,
                CACHE_OVERALL_APPOINTMENTS,
                CACHE_OVERALL_REGISTERED_PATIENTS,
                CACHE_REVENUE_TREND,
                CACHE_TODAY_APPOINTMENT_QUEUE,
                CACHE_TODAY_APPOINTMENT_QUEUE_TIME,
                CACHE_APPOINTMENT_STATUS,
                CACHE_DOCTOR_REVENUE_TRACKER);
    }
}
