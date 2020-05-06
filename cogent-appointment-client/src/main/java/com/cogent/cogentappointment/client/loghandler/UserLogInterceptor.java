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

            int status = Checkpoint.checkResponseStatus(response);

            checkExceptionAndSave(status, requestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            int status = Checkpoint.checkResponseStatus(response);

            ClientLogRequestDTO clientLogRequestDTO = convertToClientLogRequestDTO(userLog, request);
            checkExceptionAndSave(status, clientLogRequestDTO);
        }

    }

    private void checkExceptionAndSave(int status, ClientLogRequestDTO clientLogRequestDTO) {

        if (status >= 400 && status < 600) {
            clientLogRequestDTO.setLogDescription(getFailedLogDescription(clientLogRequestDTO.getFeature(), clientLogRequestDTO.getActionType(), status));
            saveFailedLogs(clientLogRequestDTO);
        }

        if (status >= 200 && status < 300) {
            clientLogRequestDTO.setLogDescription(getSuccessLogDescription(clientLogRequestDTO.getFeature(), clientLogRequestDTO.getActionType()));
            saveSuccessLogs(clientLogRequestDTO);
        }


    }


    private void saveSuccessLogs(ClientLogRequestDTO clientLogRequestDTO) {
        clientLogService.save(clientLogRequestDTO, ACTIVE);

    }

    private void saveFailedLogs(ClientLogRequestDTO clientLogRequestDTO) {
        clientLogService.save(clientLogRequestDTO, INACTIVE);
    }

}