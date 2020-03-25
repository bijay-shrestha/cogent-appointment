package com.cogent.cogentappointment.admin.configuration;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final org.springframework.web.cors.CorsConfiguration corsConfig = new org.springframework.web.cors.CorsConfiguration();
        corsConfig.setAllowCredentials(true);
//        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedOrigin("http://uat-admin-appointment.cogenthealth.com.np");
        corsConfig.addAllowedHeader("*");

        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedMethod("HEAD");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("PATCH");

        corsConfig.setAllowedHeaders(ImmutableList.of("Access-Control-Allow-Origin", "Authorization", "Cache-Control", "Content-Type", "Set-Cookie", "XSRF-TOKEN", "x-xsrf-token"));

        // This allow us to expose the headers
        corsConfig.setExposedHeaders(
                Arrays.asList(
                        "Access-Control-Allow-Headers",
                        "Access-Control-Allow-Origin",
                        "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Set-Cookie, XSRF-TOKEN"));


        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
