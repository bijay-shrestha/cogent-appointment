package com.cogent.cogentappointment.client.constants;

/**
 * @author smriti on 2019-08-16
 */
public class PatternConstants {
    public static final String ACTIVE_INACTIVE_PATTERN = "^[YN]{1}$";

    public static final String NUMBER_PATTERN="^[1-9]+[0-9]*$";

    public static final String SPECIAL_CHARACTER_PATTERN = "^[~@&!~#%*(){}+=.,]$";

    public static final String AUTHORIZATION_HEADER_PATTERN = "^(\\w+) (\\S+):(\\S+):(\\S+):(\\S+):(\\S+):(\\S+):([\\S]+)$";

    public static final String AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA = "^(\\w+) (\\S+):(\\S+):(\\S+):([\\S]+)$";

    /*yyyy-mm-dd*/
    public static final String DATE_PATTERN = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}-(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])$";

}
