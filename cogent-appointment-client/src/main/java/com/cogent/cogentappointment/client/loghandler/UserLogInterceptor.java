package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.admin.loghandler.RequestData;
import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.service.ClientLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private ClientLogService clientLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String userLog = request.getHeader("log-header");

        if (userLog != null) {

            ClientLogRequestDTO clientLogRequestDTO = RequestHandler.convertToClientLogRequestDTO(userLog);

            String clientBrowser = com.cogent.cogentappointment.admin.loghandler.RequestData.getClientBrowser(request);
            String clientOS = com.cogent.cogentappointment.admin.loghandler.RequestData.getClientOS(request);
            String clientIpAddr = RequestData.getClientIpAddr(request);

            clientLogRequestDTO.setBrowser(clientBrowser);
            clientLogRequestDTO.setOperatingSystem(clientOS);
            clientLogRequestDTO.setIpAddress(clientIpAddr);

            if (exception == null) {

                clientLogRequestDTO.setLogDescription(getSuccessLogDescription(clientLogRequestDTO.getFeature(), clientLogRequestDTO.getActionType()));
                saveSuccessLogs(clientLogRequestDTO);
            }

            if (exception != null) {

                clientLogRequestDTO.setLogDescription(getFailedLogDescription());
                saveFailedLogs(clientLogRequestDTO);
            }

        }

    }

    private void saveSuccessLogs(ClientLogRequestDTO clientLogRequestDTO) {

        clientLogService.save(clientLogRequestDTO, ACTIVE);

    }

    private void saveFailedLogs(ClientLogRequestDTO clientLogRequestDTO) {

        clientLogService.save(clientLogRequestDTO, INACTIVE);
    }

}