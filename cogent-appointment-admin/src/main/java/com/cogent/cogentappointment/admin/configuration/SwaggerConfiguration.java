package com.cogent.cogentappointment.admin.configuration;

import com.cogent.cogentappointment.admin.constants.SwaggerConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @author smriti ON 11/01/2020
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SwaggerConstants.BASE_PACKAGE))
                .paths(PathSelectors.regex(SwaggerConstants.PATH_REGEX))
                .build()
                .apiInfo(metaData())
                .globalOperationParameters(
                        Collections.singletonList(new ParameterBuilder()
                                .name("Authorization")
                                .description("HMAC Authentication Code")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)
                                .build()));
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Cogent Appointment System with esewa Integration (Admin)",
                "This is the RESTful API collections of all resources with HMAC code for default admin:" +
                        " HmacSHA512 admin:2:COGENT01:649a8184-03f5-4cad-8dfe-e1fef82ce436:406101118281836:YA/MTDLz54NO7XiXmqQnV3BKaLN88ociFo8FqjZUopCq7aUDaHnHZkp+SjPV7G6lL824Y0n/FiG138siDAbXJg==",
                "V1",
                "http://www.cogenthealth.com.np/",
                new Contact(
                        "ERP Team",
                        "https://www.f1soft.com/",
                        "erp@f1soft.com"),
                "Licensed to Cogent Health Pvt. Ltd",
                "http://www.cogenthealth.com.np/",
                java.util.Collections.emptyList());
    }
}
