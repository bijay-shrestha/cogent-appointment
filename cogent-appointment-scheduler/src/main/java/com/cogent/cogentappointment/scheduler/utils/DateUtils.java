package com.cogent.cogentappointment.scheduler.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author smriti on 2019-07-30
 */
public class DateUtils {
    public static Long getTimeInMillisecondsFromLocalDate() {
        LocalDateTime localDate = LocalDateTime.now();
        return Timestamp.valueOf(localDate).getTime();
    }

    public static Long getDifferenceBetweenTwoTime(Long startTime) {
        return getTimeInMillisecondsFromLocalDate() - startTime;
    }

    public static java.sql.Date utilDateToSqlDate(Date uDate) {
        try {
            DateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            return java.sql.Date.valueOf(sqlDateFormatter.format(uDate));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
