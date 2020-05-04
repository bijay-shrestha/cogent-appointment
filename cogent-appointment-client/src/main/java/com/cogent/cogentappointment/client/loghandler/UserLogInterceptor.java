package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.service.ClientLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.BASE_PASSWORD;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.ForgotPasswordConstants.FORGOT;
import static com.cogent.cogentappointment.client.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.client.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.client.loghandler.RequestHandler.convertToClientLogRequestDTO;
import static com.cogent.cogentappointment.client.loghandler.RequestHandler.forgotPasswordLogging;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private ClientLogService clientLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String xForwarded = RequestHeader.getXForwardedFor(request);

        if (request.getRequestURI().contains(API_V1 + BASE_PASSWORD + FORGOT)) {

            ClientLogRequestDTO requestDTO = forgotPasswordLogging(request);
            checkExceptionAndSave(exception, requestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            ClientLogRequestDTO adminLogRequestDTO = convertToClientLogRequestDTO(userLog, request);
            checkExceptionAndSave(exception, adminLogRequestDTO);
        }

    }

    private void checkExceptionAndSave(Exception exception, ClientLogRequestDTO clientLogRequestDTO) {

        if (exception == null) {
            clientLogRequestDTO.setLogDescription(getSuccessLogDescription(clientLogRequestDTO.getFeature(), clientLogRequestDTO.getActionType()));
            saveSuccessLogs(clientLogRequestDTO);
        }

        if (exception != null) {
            clientLogRequestDTO.setLogDescription(getFailedLogDescription());
            saveFailedLogs(clientLogRequestDTO);
        }
    }


    private void saveSuccessLogs(ClientLogRequestDTO clientLogRequestDTO) {
        clientLogService.save(clientLogRequestDTO, ACTIVE);

    }

    private void saveFailedLogs(ClientLogRequestDTO clientLogRequestDTO) {
        clientLogService.save(clientLogRequestDTO, INACTIVE);
    }

}