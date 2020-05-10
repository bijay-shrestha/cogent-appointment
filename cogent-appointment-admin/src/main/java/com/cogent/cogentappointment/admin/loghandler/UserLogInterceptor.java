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
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.RequestCheckpoint.*;
import static com.cogent.cogentappointment.admin.loghandler.RequestHandler.convertToAdminLogRequestDTO;
import static java.util.Arrays.asList;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String uri = request.getServletPath();
        int status = checkResponseStatus(response);

        if (asList(URL_TO_LOG).contains(uri)) {

            AdminLogRequestDTO adminLogRequestDTO = checkURI(request);
            checkStatusAndSave(response.getStatus(), adminLogRequestDTO);

        }

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            AdminLogRequestDTO adminLogRequestDTO = convertToAdminLogRequestDTO(userLog, request);
            checkStatusAndSave(response.getStatus(), adminLogRequestDTO);

        }

    }


    private void checkStatusAndSave(int status, AdminLogRequestDTO adminLogRequestDTO) {

        if (status >= 400 && status < 600) {    //error
            adminLogRequestDTO.setLogDescription(
                    getFailedLogDescription(adminLogRequestDTO.getFeature(),
                            adminLogRequestDTO.getActionType(),
                            status));
            saveFailedLogs(adminLogRequestDTO);
        }

        if (status >= 200 && status < 300) {      //success
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

}