package com.cogent.cogentappointment.client.utils.commons;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderUtils {
    public static Long getHospitalId(){
        return  (Long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }
}
