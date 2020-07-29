package com.cogent.cogentappointment.admin.constants;

/**
 * @author smriti on 2019-08-16
 */
public class PatternConstants {
    public static final String ACTIVE_INACTIVE_PATTERN = "^[YN]{1}$";

    public static final String SPECIAL_CHARACTER_PATTERN = "^[~@&!~#%*(){}+=.,]$";

    public static final String AUTHORIZATION_HEADER_PATTERN = "^(\\w+) (\\S+):(\\S+):(\\S+):(\\S+):(\\S+):(\\S+):([\\S]+)$";

}
