package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import com.cogent.cogentappointment.client.service.AuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.*;

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

        checkIfHospitalAdmin(adminMinDetails);
        checkIfPasswordIsNull(adminMinDetails);
        if (BCrypt.checkpw(requestDTO.getPassword(), adminMinDetails.getPassword())) {
            return hmacUtils.getAuthToken(adminMinDetails);
        } else {
            log.error(INVALID_PASSWORD);
            throw new NoContentFoundException(INVALID_PASSWORD);
        }

    }

    @Override
    public String loginThirdParty(LoginRequestDTO requestDTO) {
        ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailsByHospitalCode(requestDTO.getHospitalCode());

        return hmacUtils.getAuthTokenForEsewa(thirdPartyDetail);
    }

    public void checkIfHospitalAdmin(AdminMinDetails adminMinDetails) {
        if (!adminMinDetails.getIsCompany().equals('N')) {
            log.error(CANNOT_ACCESS_CLIENT_MODULE_DEBUG_MESSAGE);
            throw new BadRequestException(CANNOT_ACCESS_CLIENT_MODULE,CANNOT_ACCESS_CLIENT_MODULE_DEBUG_MESSAGE);
        }
    }

    public void checkIfPasswordIsNull(AdminMinDetails adminMinDetails) {
        if (Objects.isNull(adminMinDetails.getPassword())) {
            log.error(PASSWORD_NOT_SET_DEBUG_MESSAGE);
            throw new BadRequestException(PASSWORD_NOT_SET,PASSWORD_NOT_SET_DEBUG_MESSAGE);
        }
    }
}
