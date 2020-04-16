package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.client.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.client.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        System.out.println("pre  process-----------------------------------------------");

        return true;
    }

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

                adminLogRequestDTO.setLogDescription(adminLogRequestDTO.getFeature() + " " + adminLogRequestDTO.getActionType() + "ed...");
                saveSuccessLogs(adminLogRequestDTO, ipAddress);
            }

            if (exception != null) {

                int status = response.getStatus();
                adminLogRequestDTO.setLogDescription("Process cannot be completed due to exception...");
                saveFailedLogs(adminLogRequestDTO, ipAddress);
            }

        }

    }

    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO, String ipAddress) {

        adminLogService.save(adminLogRequestDTO, ACTIVE, ipAddress);

    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO, String ipAddress) {

        adminLogService.save(adminLogRequestDTO, INACTIVE, ipAddress);
    }

}