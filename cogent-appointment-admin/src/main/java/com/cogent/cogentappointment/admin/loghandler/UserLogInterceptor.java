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
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.getParameters;

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

        System.out.println("Method=" + request.getMethod() + "\n" +
                "URL=" + request.getRequestURI() + "\n" +
                "Params=" + getParameters(request));

        String ipAddress = RequestHandler.getRemoteAddr(request);

        String parameters = String.valueOf(request.getParameterNames());

        AdminLogRequestDTO adminLogRequestDTO = AdminLogRequestDTO
                .builder()
                .adminId(1l)
                .feature("Doctor")
                .actionType("Created")
                .parentId(1l)
                .roleId(1l)
                .logDescription("New Doctor is Created")
                .build();

        if (exception == null) {

            saveSuccessLogs(adminLogRequestDTO, ipAddress);
        }

        if (exception != null) {

            adminLogRequestDTO.setLogDescription("Process cannot be completed due to exception...");
            saveFailedLogs(adminLogRequestDTO, ipAddress);
            exception.printStackTrace();
        }

    }

    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO, String ipAddress) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, ACTIVE, ipAddress);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");

    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO, String ipAddress) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, INACTIVE, ipAddress);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");
    }

}