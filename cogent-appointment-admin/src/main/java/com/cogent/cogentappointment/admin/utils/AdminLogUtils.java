package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminLog;

import java.util.Date;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Rupak
 */
public class AdminLogUtils {

    public static AdminLog parseToAdminLog(AdminLogRequestDTO requestDTO, Character status, Admin admin) {

        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(admin);
        adminLog.setParentId(requestDTO.getParentId());
        adminLog.setRoleId(requestDTO.getRoleId());
        adminLog.setIpAddress(requestDTO.getIpAddress());
        adminLog.setFeature(requestDTO.getFeature());
        adminLog.setActionType(requestDTO.getActionType());
        adminLog.setLogDescription(requestDTO.getLogDescription());
        adminLog.setStatus(status);
        adminLog.setLogDate(utilDateToSqlDate(new Date()));
        adminLog.setLogDateTime(new Date());
        adminLog.setBrowser(requestDTO.getBrowser());
        adminLog.setOperatingSystem(requestDTO.getOperatingSystem());
        adminLog.setLocation(requestDTO.getLocation());

        return adminLog;
    }
}
