package com.cogent.cogentappointment.commons.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Slf4j
public class DateUtils {

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

    public static Long getTimeInMillisecondsFromLocalDate() {
        LocalDateTime localDate = LocalDateTime.now();
        return Timestamp.valueOf(localDate).getTime();
    }

    public static Long getDifferenceBetweenTwoTime(Long startTime) {
        return getTimeInMillisecondsFromLocalDate() - startTime;
    }

}
