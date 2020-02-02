package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;

/**
 * @author Sauravi Thapa २०/१/१९
 */
public interface AuthenticateService {
    String loginUser(LoginRequestDTO requestDTO);

    String loginThirdParty(LoginRequestDTO requestDTO);
}
