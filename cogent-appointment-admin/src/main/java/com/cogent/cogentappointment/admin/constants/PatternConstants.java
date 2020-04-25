package com.cogent.cogentappointment.admin.constants;

/**
 * @author smriti on 2019-08-16
 */
public class PatternConstants {
    public static final String ACTIVE_INACTIVE_PATTERN = "^[YN]{1}$";

    public static final String SPECIAL_CHARACTER_PATTERN = "^[~@&!~#%*(){}+=.,]$";

    public static final String AUTHORIZATION_HEADER_PATTERN = "^(\\w+) (\\S+):(\\S+):(\\S+):(\\S+):(\\S+):(\\S+):([\\S]+)$";

    public interface NetworkConstants {
        String ARP = "arp";
        String ARP_A = "a";

        String REQUEST_HEADER = "X-FORWARDED-FOR";

        String LOCALHOST_IPV6_ADDRESS = "0:0:0:0:0:0:0:1";

        String PATTERN_REGEX_FOR_MAC_OR_LINUX = "[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+";
        String PATTERN_REGEX_FOR_WINDOWS = "[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+";
    }

}
