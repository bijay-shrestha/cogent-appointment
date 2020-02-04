package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.persistence.model.Admin;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    public Admin getAdmin(String userName) {
        return adminRepository.getLoggedInAdmin(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = getAdmin(username);

        if (admin == null) throw new DataDuplicationException("USER NOT FOUND");

        return UserDetailsImpl.build(admin);
    }

}
