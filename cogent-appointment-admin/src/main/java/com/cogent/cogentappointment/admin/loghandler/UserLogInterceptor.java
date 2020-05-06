package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.loghandler.Checkpoint.checkResponseStatus;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.*;

@Component
@RestControllerAdvice
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        int status = checkResponseStatus(response);

        if (request.getRequestURI().contains(API_V1 + BASE_PASSWORD + FORGOT)) {

            AdminLogRequestDTO requestDTO = forgotPasswordLogging(request);
            checkStatusAndSave(status, requestDTO);

        }

        if (request.getRequestURI().contains(API_V1 + LOGIN) && request.getMethod().equalsIgnoreCase("POST")) {

            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);

            String body = requestWrapper.getReader().lines().collect(Collectors.joining());

            LoginRequestDTO requestDTO = ObjectMapperUtils.map(body, LoginRequestDTO.class);
            AdminLogRequestDTO adminLogRequestDTO = userLoginLogging(request, requestDTO.getEmail());
            checkStatusAndSave(status, adminLogRequestDTO);


        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            AdminLogRequestDTO adminLogRequestDTO = convertToAdminLogRequestDTO(userLog, request);
            checkStatusAndSave(status, adminLogRequestDTO);

        }

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