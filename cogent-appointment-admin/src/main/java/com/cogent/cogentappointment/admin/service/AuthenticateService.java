package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.login.LoginRequestDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sauravi Thapa २०/१/१९
 */
public interface AuthenticateService {
    String loginUser(LoginRequestDTO requestDTO);
}
