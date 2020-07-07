package com.cogent.cogentappointment.esewa;

import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import com.cogent.cogentappointment.commons.security.jwt.JwtUtils;
import com.cogent.cogentappointment.esewa.configuration.YamlPropertySourceFactory;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentTransactionRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.persistence.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages =
        {"com.cogent.cogentappointment.persistence.model",
                "com.cogent.cogentappointment.persistence.history"})
@PropertySource(
        factory = YamlPropertySourceFactory.class,
        value =
                {
                        "file:${catalina.home}/conf/eSewa/application-${spring.profiles.active}.yml"
                })
@ComponentScan(basePackages = {
        "com.cogent.cogentappointment.esewa",
        "com.cogent.cogentappointment.commons.service",
        "com.cogent.cogentappointment.commons.utils"})
@EnableJpaRepositories(basePackages = {
        "com.cogent.cogentappointment.commons.repository",
        "com.cogent.cogentappointment.esewa.repository"})
public class CogentAppointmentEsewaApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CogentAppointmentEsewaApplication.class);
    }

    public static void main(String[] args) {
//        SpringApplication.run(CogentAppointmentEsewaApplication.class, args);
        ConfigurableApplicationContext ctx =
                SpringApplication.run(CogentAppointmentEsewaApplication.class, args);

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

        PatientRequestByDTO patientRequestByDTO = new PatientRequestByDTO();
        patientRequestByDTO.setAddress("Baluwatar");
//        patientRequestByDTO.setDateOfBirth(new Date("2020-07-07"));
        patientRequestByDTO.setDistrictId(4067l);
        patientRequestByDTO.setEmail("Thapa_sauravi@hotmail.com");
        patientRequestByDTO.setESewaId("9834567678");
        patientRequestByDTO.setGender('F');
        patientRequestByDTO.setIsAgent('N');
        patientRequestByDTO.setMobileNumber("9834567678");
        patientRequestByDTO.setName("Sauravi Thapa");
        patientRequestByDTO.setProvinceId(4072l);
        patientRequestByDTO.setVdcOrMunicipalityId(4678l);
        patientRequestByDTO.setWardNumber("5");

        AppointmentTransactionRequestDTO transactionRequestDTO = new AppointmentTransactionRequestDTO();
        transactionRequestDTO.setAppointmentAmount(500D);
        transactionRequestDTO.setAppointmentModeCode("esewa");
        transactionRequestDTO.setDiscountAmount(0D);
        transactionRequestDTO.setServiceChargeAmount(0D);
        transactionRequestDTO.setTaxAmount(0D);
//        transactionRequestDTO.setTransactionDate(new Date("2070-7-12"));
        transactionRequestDTO.setTransactionNumber("test");

        AppointmentRequestDTO appointmentRequestDTO = new AppointmentRequestDTO();
        appointmentRequestDTO.setAppointmentReservationId(45l);
        appointmentRequestDTO.setCreatedDateNepali("2077-06-17");
        appointmentRequestDTO.setHospitalAppointmentServiceTypeId(2l);
        appointmentRequestDTO.setIsFollowUp('N');
        appointmentRequestDTO.setParentAppointmentId(0l);
        appointmentRequestDTO.setIsNewRegistration(true);
        appointmentRequestDTO.setPatientId(0l);

        com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForSelf appointmentRequestDTOForSelf =
                new com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTOForSelf();

        appointmentRequestDTOForSelf.setPatientInfo(patientRequestByDTO);
        appointmentRequestDTOForSelf.setAppointmentInfo(appointmentRequestDTO);
        appointmentRequestDTOForSelf.setTransactionInfo(transactionRequestDTO);

        //testing data
//        Map<String, Object> map = new HashMap<>();
//        map.p
//        map.put( appointmentRequestDTOForSelf);

        String token = JwtUtils.generateToken(appointmentRequestDTOForSelf);
        System.out.println(token);


//        map.put("appointmentDate", "2020-07-07");
//        map.put("appointmentTime", "10:00");
//        map.put("doctorId", 1);
//        map.put("hospitalId", 2);
//        map.put("patientId", 0);
//        map.put("specializationId", 1);
//        map.put("userId", "9843698393");
//        String token = JwtUtils.generateToken(map);
//        System.out.println(token);


//        map.put("transaction_code", "047Y07V");
//        map.put("product_code", "CHEERS");
//        map.put("esewa_id", "9860008245");
//        String token = JwtUtils.generateToken(map);
//        System.out.println(token);

//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Hospital For Children Eye ENT And Rehabilitation Services");
//        String token = JwtUtils.generateToken(map);
//        System.out.println(token);

    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        placeholderConfigurer.setIgnoreUnresolvablePlaceholders(true);

        return placeholderConfigurer;
    }

    @Bean
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }

    @Bean
    public MinIOProperties minIOProperties() {
        return new MinIOProperties();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

//    public static void main(String[] args) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put("name", "CHEERS");
//        String token = JwtUtils.generateToken(map);
//        System.out.println(token);
//
//    }
}
