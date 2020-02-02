package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.client.dto.request.login.LoginRequestDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.Admin;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.client.security.hmac.HMACUtils;
import com.cogent.cogentappointment.client.service.AuthenticateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.*;

/**
 * @author Sauravi Thapa २०/१/१९
 */

@Service
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    private final AuthenticationManager authenticationManager;

    private final HMACUtils hmacUtils;

    private final AdminRepository adminRepository;

    public AuthenticateServiceImpl(AuthenticationManager authenticationManager,
                                   HMACUtils hmacUtils, AdminRepository adminRepository) {
        this.authenticationManager = authenticationManager;
        this.hmacUtils = hmacUtils;
        this.adminRepository = adminRepository;
    }

    @Override
    public String loginUser(LoginRequestDTO requestDTO) {
        AdminMinDetails adminMinDetails=adminRepository.verifyLoggedInAdmin(requestDTO.getUsername(),
                requestDTO.getHospitalCode());
        if (BCrypt.checkpw(requestDTO.getPassword(), adminMinDetails.getPassword())){
            return hmacUtils.getAuthToken(adminMinDetails);
        }
        throw new NoContentFoundException(INVALID_PASSWORD);

    }
}
