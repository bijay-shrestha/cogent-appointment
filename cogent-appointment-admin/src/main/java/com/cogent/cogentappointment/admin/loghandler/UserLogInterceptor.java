package com.cogent.cogentappointment.admin.loghandler;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getFailedLogDescription;
import static com.cogent.cogentappointment.admin.loghandler.LogDescription.getSuccessLogDescription;

@Component
public class UserLogInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception exception) throws Exception {

        String userLog = RequestHeader.getUserLogs(request);

        if (userLog != null) {

            AdminLogRequestDTO adminLogRequestDTO = RequestHandler.convertToAdminLogRequestDTO(userLog);

            String clientBrowser = RequestData.getClientBrowser(request);
            String clientOS = RequestData.getClientOS(request);
            String clientIpAddr = RequestData.getClientIpAddr(request);
//            HashMap<String, Object> location=RequestData.getClientLocation(request);

            adminLogRequestDTO.setBrowser(clientBrowser);
            adminLogRequestDTO.setOperatingSystem(clientOS);
            adminLogRequestDTO.setIpAddress(clientIpAddr);

            if (exception == null) {

                adminLogRequestDTO.setLogDescription(getSuccessLogDescription(adminLogRequestDTO.getFeature(), adminLogRequestDTO.getActionType()));
                saveSuccessLogs(adminLogRequestDTO);
            }

            if (exception != null) {

                adminLogRequestDTO.setLogDescription(getFailedLogDescription());
                saveFailedLogs(adminLogRequestDTO);
            }

        }

    }

    private void saveSuccessLogs(AdminLogRequestDTO adminLogRequestDTO) {

        adminLogService.save(adminLogRequestDTO, ACTIVE);

    }

    private void saveFailedLogs(AdminLogRequestDTO adminLogRequestDTO) {

        System.out.println("SAVING USER LOGS STARTED-----------------------------------");
        adminLogService.save(adminLogRequestDTO, INACTIVE);
        System.out.println("SAVING USER LOGS COMPLETED-----------------------------------");
    }

}