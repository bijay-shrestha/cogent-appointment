package com.cogent.cogentappointment.admin.utils.commons;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author smriti on 15/06/20
 */
public class NepaliDateUtils {

    public static Date getDateFromString(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date changedDate;
        try {
            changedDate = df.parse(date);
        } catch (ParseException ex) {
            return null;
        }
        return changedDate;
    }

    public static String convertToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}
