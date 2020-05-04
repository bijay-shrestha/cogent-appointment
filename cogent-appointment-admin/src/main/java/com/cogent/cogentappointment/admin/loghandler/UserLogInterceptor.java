package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.*;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String xForwarded=RequestHeader.getXForwardedFor(request);

        if(request.getRequestURI().contains(API_V1+BASE_PASSWORD+FORGOT)){

            AdminLogRequestDTO requestDTO=forgotPasswordLogging(request);
            checkExceptionAndSave(exception,requestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            AdminLogRequestDTO adminLogRequestDTO = convertToAdminLogRequestDTO(userLog,request);
            checkExceptionAndSave(exception,adminLogRequestDTO);

        }

    }

    private void checkExceptionAndSave(Exception exception,AdminLogRequestDTO adminLogRequestDTO){

        if (exception == null) {
            adminLogRequestDTO.setLogDescription(getSuccessLogDescription(adminLogRequestDTO.getFeature(), adminLogRequestDTO.getActionType()));
            saveSuccessLogs(adminLogRequestDTO);
        }

        if (exception != null) {
            adminLogRequestDTO.setLogDescription(getFailedLogDescription());
            saveFailedLogs(adminLogRequestDTO);
        }
    }



    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO) { adminLogService.save(adminLogRequestDTO, ACTIVE); }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO) { adminLogService.save(adminLogRequestDTO, INACTIVE); }

}