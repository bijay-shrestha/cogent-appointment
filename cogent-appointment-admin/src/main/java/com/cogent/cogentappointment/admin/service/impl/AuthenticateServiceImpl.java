package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.admin.security.hmac.HMACUtils;
import com.cogent.cogentappointment.admin.service.AuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Service
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    private final AuthenticationManager authenticationManager;

    private final HMACUtils hmacUtils;

    public AuthenticateServiceImpl(AuthenticationManager authenticationManager,
                                   HMACUtils hmacUtils) {
        this.authenticationManager = authenticationManager;
        this.hmacUtils = hmacUtils;
    }

    @Override
    public String loginUser(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
            return hmacUtils.getHash(authentication);

    }
}
