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

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private ClientLogService clientLogService;

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

            ClientLogRequestDTO clientLogRequestDTO = RequestHandler.convertToClientLogRequestDTO(userLog);

            String ipAddress = RequestHandler.getRemoteAddr(request);

            if (exception == null) {

                clientLogRequestDTO.setLogDescription(clientLogRequestDTO.getFeature() + " " + clientLogRequestDTO.getActionType() + "ed...");
                saveSuccessLogs(clientLogRequestDTO, ipAddress);
            }

            if (exception != null) {

                int status = response.getStatus();
                clientLogRequestDTO.setLogDescription("Process cannot be completed due to exception...");
                saveFailedLogs(clientLogRequestDTO, ipAddress);
            }

        }

    }

    private void saveSuccessLogs(ClientLogRequestDTO clientLogRequestDTO, String ipAddress) {

        clientLogService.save(clientLogRequestDTO, ACTIVE, ipAddress);

    }

    private void saveFailedLogs(ClientLogRequestDTO clientLogRequestDTO, String ipAddress) {

        clientLogService.save(clientLogRequestDTO, INACTIVE, ipAddress);
    }

}