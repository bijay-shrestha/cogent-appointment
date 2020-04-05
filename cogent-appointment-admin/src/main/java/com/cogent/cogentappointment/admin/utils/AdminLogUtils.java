package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.persistence.model.AdminLog;

import java.util.Date;

/**
 * @author Rupak
 */
public class AdminLogUtils {

    public static AdminLog parseToAdminLog(AdminLogRequestDTO requestDTO,String ipAddress) {

        AdminLog adminLog=new AdminLog();
        adminLog.setAdminId(requestDTO.getAdminId());
        adminLog.setParentId(requestDTO.getParentId());
        adminLog.setRoleId(requestDTO.getRoleId());
        adminLog.setIpAddress(ipAddress);
        adminLog.setFeature(requestDTO.getFeature());
        adminLog.setActionType(requestDTO.getFeature());
        adminLog.setLogDescription(requestDTO.getLogDescription());
        adminLog.setStatus(requestDTO.getStatus());
        adminLog.setLogDate(DateUtils.utilDateToSqlDate(new Date()));

        return adminLog;
    }
}
