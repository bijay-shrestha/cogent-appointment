package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.response.admin.LoggedInAdminDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.persistence.model.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.USER_NOT_FOUND;


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


    public LoggedInAdminDTO getAdmin(String userName) {
        return adminRepository.getLoggedInAdmin(userName);
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        LoggedInAdminDTO loggedInAdminDTO = getAdmin(username);
        if (loggedInAdminDTO == null){
            log.error(USER_NOT_FOUND,username);
            throw new DataDuplicationException("USER NOT FOUND");
        }

        return UserDetailsImpl.build(loggedInAdminDTO);
    }

}
