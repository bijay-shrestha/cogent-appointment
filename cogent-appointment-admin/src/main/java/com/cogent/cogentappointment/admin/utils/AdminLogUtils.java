package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminLog;

import java.util.Date;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Rupak
 */
public class AdminLogUtils {

    public static AdminLog parseToAdminLog(AdminLogRequestDTO requestDTO, Admin admin, String ipAddress) {

        AdminLog adminLog=new AdminLog();
        adminLog.setAdminId(admin);
        adminLog.setParentId(requestDTO.getParentId());
        adminLog.setRoleId(requestDTO.getRoleId());
        adminLog.setIpAddress(ipAddress);
        adminLog.setFeature(requestDTO.getFeature());
        adminLog.setActionType(requestDTO.getFeature());
        adminLog.setLogDescription(requestDTO.getLogDescription());
        adminLog.setStatus(ACTIVE);
        adminLog.setLogDate(utilDateToSqlDate(new Date()));
        adminLog.setLogDateTime(utilDateToSqlDate(new Date()));

        return adminLog;
    }
}
