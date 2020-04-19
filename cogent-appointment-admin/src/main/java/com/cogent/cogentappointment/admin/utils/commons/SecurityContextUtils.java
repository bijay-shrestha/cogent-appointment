package com.cogent.cogentappointment.admin.utils.commons;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {

    public static Long getLoggedInCompanyId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    public static String getLoggedInAdminUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
