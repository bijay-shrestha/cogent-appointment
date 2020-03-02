package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.response.admin.LoggedInAdminDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.AdminRepository;
import com.cogent.cogentappointment.persistence.model.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        if (loggedInAdminDTO == null) {
            throw new NoContentFoundException("USER NOT FOUND");
        }
        return UserDetailsImpl.build(loggedInAdminDTO);
    }
}
