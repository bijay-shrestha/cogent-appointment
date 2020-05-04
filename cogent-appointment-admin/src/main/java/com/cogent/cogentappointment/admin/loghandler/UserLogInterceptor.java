package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.BASE_PASSWORD;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.convertToAdminLogRequestDTO;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.forgotPasswordLogging;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String xForwarded = RequestHeader.getXForwardedFor(request);

        if (request.getRequestURI().contains(API_V1 + BASE_PASSWORD + FORGOT)) {

            AdminLogRequestDTO requestDTO = forgotPasswordLogging(request);
            checkExceptionAndSave(exception, requestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            AdminLogRequestDTO adminLogRequestDTO = convertToAdminLogRequestDTO(userLog, request);
            checkExceptionAndSave(exception, adminLogRequestDTO);

        }

    }

    private void checkExceptionAndSave(Exception exception, AdminLogRequestDTO adminLogRequestDTO) {

        if (exception == null) {
            adminLogRequestDTO.setLogDescription(getSuccessLogDescription(adminLogRequestDTO.getFeature(), adminLogRequestDTO.getActionType()));
            saveSuccessLogs(adminLogRequestDTO);
        }

        if (exception != null) {
            adminLogRequestDTO.setLogDescription(getFailedLogDescription());
            saveFailedLogs(adminLogRequestDTO);
        }
    }


    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO) {
        adminLogService.save(adminLogRequestDTO, ACTIVE);
    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO) {
        adminLogService.save(adminLogRequestDTO, INACTIVE);
    }

}