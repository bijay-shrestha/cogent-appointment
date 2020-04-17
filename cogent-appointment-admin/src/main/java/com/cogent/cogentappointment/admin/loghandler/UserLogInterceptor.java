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
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        System.out.println("ACTION COMPLETED-----------------------------------");

        String url = request.getRequestURI();

        String userLog = request.getHeader("log-header");

        if (userLog != null) {

            AdminLogRequestDTO adminLogRequestDTO = RequestHandler.convertToAdminLogRequestDTO(userLog);

            String ipAddress = RequestHandler.getRemoteAddr(request);

            if (exception == null) {

                adminLogRequestDTO.setLogDescription(getSuccessLogDescription(adminLogRequestDTO.getFeature(), adminLogRequestDTO.getActionType()));
                saveSuccessLogs(adminLogRequestDTO, ipAddress);
            }

            if (exception != null) {

                adminLogRequestDTO.setLogDescription(getFailedLogDescription());
                saveFailedLogs(adminLogRequestDTO, ipAddress);
            }

        }

    }

    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO, String ipAddress) {

        adminLogService.save(adminLogRequestDTO, ACTIVE, ipAddress);

    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO, String ipAddress) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, INACTIVE, ipAddress);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");
    }

}