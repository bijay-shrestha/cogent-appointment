package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.AdminLogRequestDTO;

/**
 * @author Rupak
 */
public interface AdminLogService {

    void save(AdminLogRequestDTO adminRequestDTO, Character status, String ipAddress);

}
