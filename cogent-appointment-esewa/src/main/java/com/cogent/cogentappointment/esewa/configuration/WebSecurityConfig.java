package com.cogent.cogentappointment.esewa.configuration;

import com.cogent.cogentappointment.esewa.exception.authentication.AuthEntryPointHmac;
import com.cogent.cogentappointment.esewa.pki.filter.PKIFilter;
import com.cogent.cogentappointment.esewa.pki.wrapper.DataWrapper;
import com.cogent.cogentappointment.esewa.repository.PKIAuthenticationInfoRepository;
import com.cogent.cogentappointment.esewa.security.filter.HmacAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.cogent.cogentappointment.esewa.constants.SwaggerConstants.AUTH_WHITELIST;


/**
 * @author Sauravi Thapa २०/१/१४
 */

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthEntryPointHmac unauthorizedHandler;

    private final HmacAuthenticationFilter hmacAuthenticationFilter;

    private final PKIFilter pkiFilter;

    private final DataWrapper dataWrapper;

    private final PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository;

    public WebSecurityConfig(AuthEntryPointHmac unauthorizedHandler,
                             HmacAuthenticationFilter hmacAuthenticationFilter,
                             PKIFilter pkiFilter, DataWrapper dataWrapper,
                             PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.hmacAuthenticationFilter = hmacAuthenticationFilter;
        this.pkiFilter = pkiFilter;
        this.dataWrapper = dataWrapper;
        this.pkiAuthenticationInfoRepository = pkiAuthenticationInfoRepository;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilterBefore(hmacAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new PKIFilter(pkiAuthenticationInfoRepository, dataWrapper),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //    allow swagger
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated();
    }

//    @Bean
//    public FilterRegistrationBean<PKIFilter> pkiFilter() {
//        FilterRegistrationBean<PKIFilter> registrationBean
//                = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(new PKIFilter(pkiAuthenticationInfoRepository, dataWrapper));
//        registrationBean.addUrlPatterns("/*");
//
//        return registrationBean;
//    }

}
