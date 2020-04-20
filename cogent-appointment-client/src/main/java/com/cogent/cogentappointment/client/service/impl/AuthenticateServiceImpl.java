package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import com.cogent.cogentappointment.client.service.AuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.*;

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
    public String loginThirdParty(LoginRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(null);

        return hmacUtils.getAuthTokenForEsewa(thirdPartyDetail);
    }
}
