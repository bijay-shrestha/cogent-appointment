package com.cogent.cogentappointment.esewa.exception.authentication;

import com.cogent.cogentappointment.esewa.dto.response.login.LoginErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.INVALID_PASSWORD;

/**
 * @author Sauravi Thapa २०/१/२०
 */

@Component
public class AuthEntryPointHmac implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        LoginErrorResponse loginResponse = LoginErrorResponse.builder()
                .status(401)
                .errorMessage(INVALID_PASSWORD)
                .build();

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(loginResponse);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        response.getWriter().write(json);
        response.setStatus(loginResponse.getStatus());
        response.flushBuffer();

    }
}
