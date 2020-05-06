package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
@RestControllerAdvice
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

//        String xForwarded = RequestHeader.getXForwardedFor(request);

        if (request.getRequestURI().contains(API_V1 + BASE_PASSWORD + FORGOT)) {

            int status = Checkpoint.checkResponseStatus(response);

            AdminLogRequestDTO requestDTO = forgotPasswordLogging(request);
            checkStatusAndSave(status, requestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            int status = Checkpoint.checkResponseStatus(response);

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