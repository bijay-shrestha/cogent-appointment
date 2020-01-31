package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import com.cogent.cogentappointment.client.service.AuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
    public String loginUser(HttpServletRequest request, LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        return hmacUtils.getAuthTokenForEsewa(request);
    }
}
