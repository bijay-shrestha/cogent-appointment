package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.*;
import static com.cogent.cogentappointment.admin.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.admin.loghandler.Checkpoint.checkResponseStatus;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.forgotPasswordLogging;

@Component
public class UserLogInterceptor extends GenericFilterBean implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (uri.contains(API_V1 + LOGIN) && method.equalsIgnoreCase("POST")) {

            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            String swaggerJson = new String(requestWrapper.getContentAsByteArray());

            LoginRequestDTO requestDTO = ObjectMapperUtils.map(swaggerJson, LoginRequestDTO.class);

            System.out.println(swaggerJson);

//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                String json = mapper.writeValueAsString(cat);
//                System.out.println("ResultingJSONstring = " + json);
//                //System.out.println(json);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();


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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
            responseWrapper.copyBodyToResponse();

        } finally {
            String requestBody = new String(requestWrapper.getContentAsByteArray());
            System.out.println(requestBody);

            String responseBody = new String(responseWrapper.getContentAsByteArray());
            System.out.println(responseBody);

            // Do not forget this line after reading response content or actual response will be empty!
        }

    }
}