package com.cogent.cogentappointment.admin.configuration;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;

@Component
public class MyLogInterceptor implements HandlerInterceptor {


    private final AdminLogService adminLogService;

    public MyLogInterceptor(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        long executionStartTime = System.currentTimeMillis();
//        request.setAttribute("start-time", executionStartTime);
//        String fishTag = UUID.randomUUID().toString();
//        String operationPath = this.contextPath + request.getServletPath();
//        String user = "myuser"; /* GET USER INFORMATION FROM SESSION */
//        ThreadContext.put("id", fishTag);
//        ThreadContext.put("path", operationPath);
//        ThreadContext.put("user", user);

        System.out.println("pre  process-----------------------------------------------");

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {


        System.out.println("ACTION COMPLETED-----------------------------------");

        AdminLogRequestDTO adminLogRequestDTO = AdminLogRequestDTO
                .builder()
                .adminId(1l)
                .feature("Hospital")
                .actionType("Created")
                .parentId(1l)
                .roleId(1l)
                .logDescription("Hospital is Created")
                .build();


        if (exception.getStackTrace() == null) {

            saveSuccessLogs(adminLogRequestDTO, request);

        }

        if (exception.getStackTrace() != null) {

            adminLogRequestDTO.setLogDescription(exception.getLocalizedMessage());

            saveFailedLogs(adminLogRequestDTO, request);
            exception.printStackTrace();

        }


    }

    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, StatusConstants.ACTIVE, httpServletRequest);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");


    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, INACTIVE, httpServletRequest);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");


    }

}