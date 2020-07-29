package com.cogent.cogentappointment.admin.utils.commons;

import com.cogent.cogentappointment.admin.constants.PatternConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author smriti on 2019-08-16
 */
public class PatternUtils {

    public static boolean isStatusActiveOrInactive(Character status) {
        Pattern pattern = Pattern.compile(PatternConstants.ACTIVE_INACTIVE_PATTERN);
        Matcher m = pattern.matcher(status.toString());
        return m.find();
    }

    public static boolean hasSpecialCharacter(String value){
        Pattern pattern = Pattern.compile(PatternConstants.SPECIAL_CHARACTER_PATTERN);
        Matcher m = pattern.matcher(value);
        return m.find();
    }
}
