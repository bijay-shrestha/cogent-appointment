package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ClientLog;

import java.util.Date;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author Rupak
 */
public class ClientLogUtils {

    public static ClientLog parseToClientLog(ClientLogRequestDTO requestDTO, Character status, Admin admin, String ipAddress) {

        ClientLog clientLog = new ClientLog();
        clientLog.setAdminId(admin);
        clientLog.setParentId(requestDTO.getParentId());
        clientLog.setRoleId(requestDTO.getRoleId());
        clientLog.setIpAddress(ipAddress);
        clientLog.setFeature(requestDTO.getFeature());
        clientLog.setActionType(requestDTO.getActionType());
        clientLog.setLogDescription(requestDTO.getLogDescription());
        clientLog.setStatus(status);
        clientLog.setLogDate(utilDateToSqlDate(new Date()));
        clientLog.setLogDateTime(new Date());

        return clientLog;
    }
}
