package com.cogent.cogentappointment.esewa.security.filter;

import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.RequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.cogent.cogentappointment.esewa.constants.HMACConstant.HMAC_API_SECRET_ESEWA;

/**
 * @author Sauravi Thapa ON 7/7/20
 */
@Slf4j
@Component
public class JwtRequestFilter  implements Filter{

        public static final String DATA = "data";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
