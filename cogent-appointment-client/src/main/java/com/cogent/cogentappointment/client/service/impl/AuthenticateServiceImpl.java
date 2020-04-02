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

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.CANNOT_ACCESS_CLIENT_MODULE;
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
                requestDTO.getHospitalCode());
        if (adminMinDetails.getIsCompany().equals('N')) {
            if (BCrypt.checkpw(requestDTO.getPassword(), adminMinDetails.getPassword())) {
                return hmacUtils.getAuthToken(adminMinDetails);
            } else {
                log.error(INVALID_PASSWORD);
                throw new NoContentFoundException(INVALID_PASSWORD);
            }
        } else
            log.error(CANNOT_ACCESS_CLIENT_MODULE);
            throw new NoContentFoundException(CANNOT_ACCESS_CLIENT_MODULE);
    }

    @Override
    public String loginThirdParty(LoginRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(requestDTO.getHospitalCode());

        return hmacUtils.getAuthTokenForEsewa(thirdPartyDetail);
    }
}
