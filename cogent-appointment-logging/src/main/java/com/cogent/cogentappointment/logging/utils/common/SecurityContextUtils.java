package com.cogent.cogentappointment.logging.utils.common;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {

    public static Long getLoggedInCompanyId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
