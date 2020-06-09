package com.cogent.cogentappointment.thirdparty.configuration;

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


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cogent.cogentappointment.thirdparty.resource"))
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(metaData())
                .globalOperationParameters(
                        Collections.singletonList(new ParameterBuilder ()
                                .name("Authorization")
                                .description("HMAC Authentication Code")
                                .modelRef(new ModelRef ("string"))
                                .parameterType("header")
                                .required(false)
                                .build()));
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Third Party Integration (Tester)",
                "This will be used to integrate Third party APIs",
                "V1",
                "http://www.cogenthealth.com.np/",
                new Contact(
                        "ERP Team",
                        "https://www.f1soft.com/",
                        "erp@f1soft.com"),
                "Licensed to Cogent Health Pvt. Ltd",
                "http://www.cogenthealth.com.np/",
                Collections.emptyList());
    }
}
