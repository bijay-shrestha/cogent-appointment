package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.AdminLogRequestDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rupak
 */
public interface AdminLogService {

    void save(AdminLogRequestDTO adminRequestDTO,Character status, HttpServletRequest httpServletRequest);

}
