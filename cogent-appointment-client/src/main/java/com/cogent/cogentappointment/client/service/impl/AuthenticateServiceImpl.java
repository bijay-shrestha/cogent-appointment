package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.login.LoginEsewaRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import com.cogent.cogentappointment.client.service.AuthenticateService;
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

    private final HmacApiInfoRepository hmacApiInfoRepository;

    public AuthenticateServiceImpl(AuthenticationManager authenticationManager, HMACUtils hmacUtils,
                                   HmacApiInfoRepository hmacApiInfoRepository) {
        this.authenticationManager = authenticationManager;
        this.hmacUtils = hmacUtils;
        this.hmacApiInfoRepository = hmacApiInfoRepository;
    }

    @Override
    public String loginUser(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
        return hmacUtils.getHash(authentication);
    }

    @Override
    public String loginThirdParty(LoginEsewaRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(requestDTO.getCompanyCode());

        return hmacUtils.getAuthTokenForEsewa(thirdPartyDetail);
    }
}
