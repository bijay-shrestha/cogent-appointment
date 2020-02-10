package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import com.cogent.cogentappointment.client.service.AuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_PASSWORD;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Service
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    private final HMACUtils hmacUtils;

    private final HmacApiInfoRepository hmacApiInfoRepository;

    public AuthenticateServiceImpl(HMACUtils hmacUtils,
                                   HmacApiInfoRepository hmacApiInfoRepository) {
        this.hmacUtils = hmacUtils;
        this.hmacApiInfoRepository = hmacApiInfoRepository;
    }

    @Override
    public String loginUser(LoginRequestDTO requestDTO) {
        AdminMinDetails adminMinDetails = hmacApiInfoRepository.verifyLoggedInAdmin(requestDTO.getUsername(),
                requestDTO.getCompanyCode());
        if (BCrypt.checkpw(requestDTO.getPassword(), adminMinDetails.getPassword())) {
            return hmacUtils.getAuthToken(adminMinDetails);
        }
        throw new NoContentFoundException(INVALID_PASSWORD);

    }

    @Override
    public String loginThirdParty(LoginRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(requestDTO.getCompanyCode());

        return hmacUtils.getAuthTokenForEsewa(thirdPartyDetail);
    }
}
