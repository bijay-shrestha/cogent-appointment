package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.LOGIN;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.LOGOUT;
import static com.cogent.cogentappointment.client.loghandler.RequestHandler.*;

public class RequestCheckpoint {

    public static final CharSequence[] URL_TO_LOG = {
            "/api/v1/login",
            "/api/v1/password/forgot",
            "/api/v1/logout"
    };

    public static int checkResponseStatus(HttpServletResponse response) {

        return response.getStatus();
    }

    public static ClientLogRequestDTO checkURI(HttpServletRequest request, LoginRequestDTO loginRequestDTO) throws IOException, GeoIp2Exception {

        String method = request.getMethod();
        ClientLogRequestDTO clientLogRequestDTO = null;
        if (request.getServletPath().contains(LOGIN) && method.equalsIgnoreCase("POST")) {

            String email = loginRequestDTO.getEmail();
            clientLogRequestDTO = userLoginLogging(request, email);

        }

        if (request.getServletPath().contains(FORGOT)) {

            clientLogRequestDTO = forgotPasswordLogging(request);

        }

        if (request.getServletPath().contains(LOGOUT)) {

            clientLogRequestDTO = userLogoutLogging(request);

        }

        return clientLogRequestDTO;


    }
}
