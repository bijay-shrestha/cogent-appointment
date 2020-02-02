package com.cogent.cogentappointment.client.constants;

/**
 * @author smriti on 2019-08-16
 */
public class PatternConstants {
    public static final String ACTIVE_INACTIVE_PATTERN = "^[YN]{1}$";

    public static final String EMAIL_PATTERN = "^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$";

    public static final String MAC_ADDRESS_PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

    public static final String SPECIAL_CHARACTER_PATTERN = "^[~@&!~#%*(){}+=.,]$";

    public static final String AUTHORIZATION_HEADER_PATTERN = "^(\\w+) (\\S+):(\\S+):(\\S+):(\\S+):([\\S]+)$";

    public static final String AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA = "^(\\w+) (\\S+):(\\S+):([\\S]+)$";
}
