package com.cogent.cogentappointment.esewa.utils.commons;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {

    public static Long getLoggedInHospitalId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
