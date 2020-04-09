package com.cogent.cogentappointment.admin.configuration;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


//    @Override
//    public boolean postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {
////        long executionStartTime = (Long)request.getAttribute("start-time");
////        long renderingStartTime = System.currentTimeMillis();
////        request.setAttribute("rendering-start-time", renderingStartTime);
////        long executionDuration = renderingStartTime - executionStartTime;
////        ThreadContext.put("execution-duration", executionTime);
//    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {


        System.out.println("ACTION COMPLETED-----------------------------------");



    }


    private void saveLogs(AdminLogRequestDTO adminLogRequestDTO, HttpServletRequest httpServletRequest) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, httpServletRequest);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");


    }

}