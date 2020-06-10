package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.service.ClientLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.client.loghandler.RequestCheckpoint.checkURI;
import static com.cogent.cogentappointment.client.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.client.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.client.loghandler.RequestHandler.convertToClientLogRequestDTO;
import static com.cogent.cogentappointment.client.utils.commons.ObjectMapperUtils.map;
import static java.util.Arrays.asList;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private ClientLogService clientLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        int status = RequestCheckpoint.checkResponseStatus(response);
        String uri = request.getServletPath();
        if (asList(RequestCheckpoint.URL_TO_LOG).contains(uri)) {

            Object data=request.getAttribute("loginRequest");

            LoginRequestDTO loginRequestDTO=null;
            if(data!=null){
                loginRequestDTO = map(data.toString(), LoginRequestDTO.class);
            }


            ClientLogRequestDTO clientLogRequestDTO = checkURI(request, loginRequestDTO);
            checkStatusAndSave(status, clientLogRequestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            ClientLogRequestDTO clientLogRequestDTO = convertToClientLogRequestDTO(userLog, request);
            checkStatusAndSave(status, clientLogRequestDTO);
        }

    }

    private void checkStatusAndSave(int status, ClientLogRequestDTO clientLogRequestDTO) {

        if (status >= 400 && status < 600) {   //error
            clientLogRequestDTO.setLogDescription(getFailedLogDescription(clientLogRequestDTO.getFeature(), clientLogRequestDTO.getActionType(), status));
            saveFailedLogs(clientLogRequestDTO);
        }

        if (status >= 200 && status < 300) {   //success
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