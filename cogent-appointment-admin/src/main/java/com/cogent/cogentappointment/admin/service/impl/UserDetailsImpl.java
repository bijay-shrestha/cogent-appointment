package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.response.admin.LoggedInAdminDTO;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Sauravi Thapa २०/१/१६
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String email;

    private Character isCompany;

    @JsonIgnore
    private String password;

    private String companyCode;

    private Long companyId;

    private String apiKey;

    private String apiSecret;


    public UserDetailsImpl(Long id,
                           String email,
                           Character isCompany,
                           String password,
                           String companyCode,
                           Long companyId,
                           String apiKey,
                           String apiSecret) {
        this.id = id;
        this.email = email;
        this.isCompany = isCompany;
        this.password = password;
        this.companyCode = companyCode;
        this.companyId = companyId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public static UserDetailsImpl build(LoggedInAdminDTO admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getEmail(),
                admin.getIsCompany(),
                admin.getPassword(),
                admin.getCompanyCode(),
                admin.getCompanyId(),
                admin.getApiKey(),
                admin.getApiSecret());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
