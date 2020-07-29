package com.cogent.cogentappointment.logging.configuration;

import com.cogent.cogentappointment.logging.constants.SwaggerConstants;
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
                                .required(false)
                                .build()));
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Cogent Appointment System with esewa Integration (Logging)",
                "HMAC CODE FROM ADMIN : HmacSHA512 1:admin@gmail.com:1:COGENT:649a8184-03f5-4cad-8dfe-e1fef82ce436:046989728535027:aknsTQkwFPDS3Ku/Zqnd1Jmbc7COzck3oESD8F4mSaAdyW2gTPoPuOwMsZqogzDlTpmVl8hVEi/GpgdA8Ge/Mg== \n" +
                        "\n HMAC CODE FROM CLIENT :  HmacSHA512 2:client@gmail.com:2:HAMS:3ef50da9-49e1-4ff0-8b30-40fdb4545bac:556833931635949:HiXv+ciDDtVwjAb9EocVsa/Kv8v6YisDiKWt6uVw8VoNJKp0PHoOHBiaLJ/ZwyD/tmhfo9ur2a/y6kTenmG8+Q== ",
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
