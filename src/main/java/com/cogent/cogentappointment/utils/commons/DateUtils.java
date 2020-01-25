package com.cogent.cogentappointment.utils.commons;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static com.cogent.cogentappointment.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.constants.UtilityConfigConstants.*;

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

    public static Date addDays(Date oldDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static LocalDate convertDateToLocalDate(Date date){
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static String getDayCodeFromDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EE");
        return dateFormat.format(date);
    }

    public static boolean isLocalDateBetweenInclusive(LocalDate startDate, LocalDate endDate, LocalDate target) {
        return !target.isBefore(startDate) && !target.isAfter(endDate);
    }

    public static boolean isDateBetweenInclusive(Date startDate, Date endDate, Date target) {
        return !target.before(startDate) && !target.after(endDate);
    }

    public static Date convertStringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public static int getYearFromNepaliDate(String nepaliDate) {
        return Integer.parseInt(nepaliDate.split(HYPHEN)[0]);
    }

    public static int getMonthFromNepaliDate(String nepaliDate) {
        return Integer.parseInt(nepaliDate.split(HYPHEN)[1]);
    }

    public static String getDayFromNepaliDate(String nepaliDate) {
        return nepaliDate.split(HYPHEN)[2];
    }

    public static String fetchStartingFiscalYear(int year, int month) {
        return (month < APPLICATION_STARTING_FISCAL_MONTH)
                ? (year + 1 + APPLICATION_STARTING_FISCAL_DAY) : (year + APPLICATION_STARTING_FISCAL_DAY);
    }

    public static String fetchEndingFiscalYear(int year, int month) {
        return (month < APPLICATION_STARTING_FISCAL_MONTH)
                ? (year + APPLICATION_ENDING_FISCAL_DAY) : (year + 1 + APPLICATION_ENDING_FISCAL_DAY);
    }

    public static String getTimeIn12HourFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }
}
