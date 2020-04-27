package com.cogent.cogentappointment.admin.configuration;

import com.cogent.cogentappointment.admin.exception.authentication.AuthEntryPointHmac;
import com.cogent.cogentappointment.admin.security.filter.HmacAuthenticationFilter;
import com.cogent.cogentappointment.admin.security.hmac.HMACConfig;
import com.cogent.cogentappointment.admin.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.cogent.cogentappointment.admin.constants.SwaggerConstants.AUTH_WHITELIST;

/**
 * @author Sauravi Thapa २०/१/१४
 */

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final HMACConfig hmaconfig;

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointHmac unauthorizedHandler;

    private final HmacAuthenticationFilter hmacAuthenticationFilter;

    public WebSecurityConfig(HMACConfig hmaconfig,
                             UserDetailsServiceImpl userDetailsService,
                             AuthEntryPointHmac unauthorizedHandler, HmacAuthenticationFilter hmacAuthenticationFilter) {
        this.hmaconfig = hmaconfig;
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.hmacAuthenticationFilter = hmacAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilterBefore(hmacAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //    allow swagger
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, hmaconfig.getUri()).permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/file/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/admin/verify").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/admin/password").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/password/forgot").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/password/verify").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/password").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/hello/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/hello").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/admin/verify/email").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/companyAdmin/verify/email").permitAll()

                .anyRequest().authenticated();
    }

    // configure AuthenticationManager so that it knows from where to load
    // user for matching credentials
    // Use BCryptPasswordEncoder
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
