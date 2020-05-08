package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.LOGIN;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.LOGOUT;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.*;

public class RequestCheckpoint {

    public static final CharSequence[] URL_TO_LOG = {
            "/api/v1/login",
            "/api/v1/password/forgot",
            "/api/v1/logout"
    };

    public static int checkResponseStatus(HttpServletResponse response) {
        return response.getStatus();
    }

    public static AdminLogRequestDTO checkURI(HttpServletRequest request, HttpServletResponse response) throws IOException, GeoIp2Exception {

        String method = request.getMethod();
        AdminLogRequestDTO clientLogRequestDTO = null;
        if (request.getServletPath().contains(LOGIN) && method.equalsIgnoreCase("POST")) {

            String email = response.getHeader("email");
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
