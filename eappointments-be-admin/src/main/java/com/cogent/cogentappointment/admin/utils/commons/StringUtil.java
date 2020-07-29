package com.cogent.cogentappointment.admin.utils.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * @author smriti on 2019-08-27
 */
public class StringUtil {
    public static String urlConverter(String subDirectory, String toReplace, String replaceWith) {
        return subDirectory.replaceAll(toReplace, replaceWith);
    }

    public static String splitByCharacterTypeCamelCase(String name) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase
                (name.replaceAll("\\d+", "")), " ");
    }

    public static String convertToNormalCase(String text) {

        final char[] delimiters = {' ', '_'};
        return WordUtils.capitalizeFully(text, delimiters);
    }

    public static String toUpperCase(String name) {
        return name.toUpperCase();
    }
}
