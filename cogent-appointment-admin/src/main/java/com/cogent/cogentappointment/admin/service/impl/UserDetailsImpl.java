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

    private String username;

    private Character isCogentAdmin;

    @JsonIgnore
    private String password;


    public UserDetailsImpl(Long id, String username, Character isCogentAdmin, String password) {
        this.id = id;
        this.username = username;
        this.isCogentAdmin = isCogentAdmin;
        this.password = password;
    }

    public static UserDetailsImpl build(LoggedInAdminDTO admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getUsername(),
                admin.getIsCogentAdmin(),
                admin.getPassword());
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
        return username;
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
