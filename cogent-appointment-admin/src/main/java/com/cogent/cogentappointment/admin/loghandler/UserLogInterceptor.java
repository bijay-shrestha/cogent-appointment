package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.loghandler.Checkpoint.checkResponseStatus;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.forgotPasswordLogging;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (uri.contains(API_V1 + LOGIN) && method.equalsIgnoreCase("POST")) {

            ServletInputStream a = request.getInputStream();
            String requestStr = IOUtils.toString(a);
            LoginRequestDTO adminRequestDTO = ObjectMapperUtils.map(requestStr, LoginRequestDTO.class);

            System.out.println(adminRequestDTO.toString());


//            getInputStream(requestWrapper.getContentAsByteArray());

//            LoginRequestDTO requestDTO = ObjectMapperUtils.map(requestBody, LoginRequestDTO.class);
//
//            AdminLogRequestDTO adminLogRequestDTO = userLoginLogging(request, requestDTO.getEmail());
//            checkStatusAndSave(status, adminLogRequestDTO);


        }

        int status = checkResponseStatus(response);

        if (request.getRequestURI().contains(API_V1 + BASE_PASSWORD + FORGOT)) {

            AdminLogRequestDTO requestDTO = forgotPasswordLogging(request);
            checkStatusAndSave(status, requestDTO);

        }


        String userLog = RequestHeader.getUserLogs(request);

//        if (userLog != null) {
//
//            AdminLogRequestDTO adminLogRequestDTO = convertToAdminLogRequestDTO(userLog, request);
//            checkStatusAndSave(status, adminLogRequestDTO);
//
//        }

    }

    private void checkStatusAndSave(int status, AdminLogRequestDTO adminLogRequestDTO) {

        if (status >= 400 && status < 600) {
            adminLogRequestDTO.setLogDescription(
                    getFailedLogDescription(adminLogRequestDTO.getFeature(),
                            adminLogRequestDTO.getActionType(),
                            status));
            saveFailedLogs(adminLogRequestDTO);
        }

        if (status >= 200 && status < 300) {
            adminLogRequestDTO.setLogDescription(getSuccessLogDescription(adminLogRequestDTO.getFeature(), adminLogRequestDTO.getActionType()));
            saveSuccessLogs(adminLogRequestDTO);
        }


    }


    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO) {
        adminLogService.save(adminLogRequestDTO, ACTIVE);
    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO) {
        adminLogService.save(adminLogRequestDTO, INACTIVE);
    }
}