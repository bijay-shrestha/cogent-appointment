package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.response.admin.LoggedInAdminDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.USER_NOT_FOUND;


/**
 * @author Sauravi Thapa २०/१/१३
 */

@Service
@Transactional
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public LoggedInAdminDTO getAdmin(String email) {

        try {
            return adminRepository.getLoggedInAdmin(email);
        } catch (NoContentFoundException ex) {
            throw new NoContentFoundException("USER NOT FOUND");
        }
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        LoggedInAdminDTO loggedInAdminDTO = null;

//        if (loggedInAdminDTO == null) {
//            log.error(USER_NOT_FOUND, email);
//            throw new NoContentFoundException("USER NOT FOUND");
//        }

        try {
            loggedInAdminDTO = getAdmin(email);
        } catch (NoContentFoundException e) {
            log.error(USER_NOT_FOUND, email);
            throw new NoContentFoundException("USER NOT FOUND");
        }
        return UserDetailsImpl.build(loggedInAdminDTO);
    }
}
