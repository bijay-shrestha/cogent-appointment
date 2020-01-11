package com.cogent.cogentappointment.utils.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cogent.cogentappointment.constants.PatternConstants.ACTIVE_INACTIVE_PATTERN;
import static com.cogent.cogentappointment.constants.PatternConstants.SPECIAL_CHARACTER_PATTERN;

/**
 * @author smriti on 2019-08-16
 */
public class PatternUtils {

    public static boolean isStatusActiveOrInactive(Character status) {
        Pattern pattern = Pattern.compile(ACTIVE_INACTIVE_PATTERN);
        Matcher m = pattern.matcher(status.toString());
        return m.find();
    }

    public static boolean hasSpecialCharacter(String value){
        Pattern pattern = Pattern.compile(SPECIAL_CHARACTER_PATTERN);
        Matcher m = pattern.matcher(value);
        return m.find();
    }
}
