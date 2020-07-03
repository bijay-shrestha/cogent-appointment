package com.cogent.cogentappointment.client.utils.commons;

import com.cogent.cogentappointment.client.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.*;
import static com.cogent.cogentappointment.client.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.client.constants.UtilityConfigConstants.*;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * @author smriti on 2019-07-30
 */
@Slf4j
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

    public static String utilDateToSqlDateInString(Date uDate) {
        try {
            DateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            return java.sql.Date.valueOf(sqlDateFormatter.format(uDate)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static List<Date> utilDateListToSqlDateList(List<Date> uDates) {
        List<Date> resultDates = new ArrayList<>();
        uDates.forEach(uDate -> {
            try {
                DateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = java.sql.Date.valueOf(sqlDateFormatter.format(uDate));
                resultDates.add(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return resultDates;

    }

    public static Date addDays(Date oldDate, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static LocalDate convertDateToLocalDate(Date date) {
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
        Date targetDateOnly = removeTime(target);
        Date startDateOnly = removeTime(startDate);
        Date endDateOnly = removeTime(endDate);
        return !targetDateOnly.before(startDateOnly) && !targetDateOnly.after(endDateOnly);
    }

    public static Date convertStringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public static String convertDateToString(Date date) throws ParseException {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
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


    /*IF REQUESTED YEAR IS ODD,
    *   IF MONTH <4
    *       S = YEAR - 1 + START_FISCAL_DAY
    *       E = YEAR + END_FISCAL_DAY
    *   ELSE
    *       S = YEAR + END_FISCAL_DAY
    *       E = YEAR + 1 + START_FISCAL-DAY
    * ELSE
    *   IF MONTH <4
    *       S = YEAR - 1 + END_FISCAL_DAY
    *       E = YEAR + START_FISCAL_DAY
    *   ELSE
    *       S = YEAR + START_FISCAL_DAY
    *       E = YEAR + 1 + END_FISCAL_DAY
    * */
    public static String fetchStartingFiscalYear(int year, int month) {
        if (year % 2 == 0) {
            //selected year is even
            return (month < APPLICATION_STARTING_FISCAL_MONTH)
                    ? (year - 1 + APPLICATION_ENDING_FISCAL_DAY)
                    : (year + APPLICATION_STARTING_FISCAL_DAY);
        } else {
            //selected year is odd
            return (month < APPLICATION_STARTING_FISCAL_MONTH)
                    ? (year - 1 + APPLICATION_STARTING_FISCAL_DAY)
                    : (year + APPLICATION_ENDING_FISCAL_DAY);
        }
    }

    public static String fetchEndingFiscalYear(int year, int month) {
        if (year % 2 == 0) {
            //selected year is even
            return (month < APPLICATION_STARTING_FISCAL_MONTH)
                    ? (year + APPLICATION_STARTING_FISCAL_DAY)
                    : (year + 1 + APPLICATION_ENDING_FISCAL_DAY);
        } else {
            //selected year is odd
            return (month < APPLICATION_STARTING_FISCAL_MONTH)
                    ? (year + APPLICATION_ENDING_FISCAL_DAY)
                    : (year + 1 + APPLICATION_STARTING_FISCAL_DAY);
        }
    }

    public static String getTimeIn12HourFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }

    public static String getTimeFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }

    public static String convert24HourTo12HourFormat(String timeIn24HrFormat) {
        try {
            SimpleDateFormat dateParser = new SimpleDateFormat("HH:mm");
            Date date = dateParser.parse(timeIn24HrFormat);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a");
            return dateFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convert12HourTo24HourFormat(String timeIn12HrFormat) {
        int hour = Integer.parseInt(timeIn12HrFormat.substring(0, 2)) % 12;
        if (timeIn12HrFormat.endsWith("PM") || timeIn12HrFormat.endsWith("pm"))
            hour += 12;
        return String.format("%02d", hour) + timeIn12HrFormat.substring(2, 6);
    }

    public static Date parseTime(String requestedTime) {
        try {
            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
            return time.parse(requestedTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date datePlusTime(Date date, Date time) {

        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.HOUR, time.getHours());
        cl.add(Calendar.MINUTE, time.getMinutes());

        return cl.getTime();
    }

    public static boolean isFirstDateGreater(Date dateA, Date dateB) {

        Calendar calA = Calendar.getInstance();
        calA.setTime(dateA);

        Calendar calB = Calendar.getInstance();
        calB.setTime(dateB);

        if (calA.get(YEAR) > calB.get(YEAR)) {
            return true;
        } else if (calA.get(YEAR) == calB.get(YEAR)) {
            if (calA.get(MONTH) > calB.get(MONTH)) {
                return true;
            } else if (calA.get(MONTH) == calB.get(MONTH)) {
                return calA.get(Calendar.DAY_OF_MONTH) > calB.get(Calendar.DAY_OF_MONTH);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static List<Date> getDates(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Date today = utilDateToSqlDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (!calendar.after(endCalendar)) {
            Date result = calendar.getTime();
            if (!utilDateToSqlDate(calendar.getTime()).before(today)) {
                datesInRange.add(result);
            }
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static Date convertLocalDateToDate(LocalDate requestedDate) {
        return java.util.Date.from(requestedDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static boolean conditionOfBothDateProvided(Date fromDate, Date toDate) {
        return !Objects.isNull(fromDate) && !Objects.isNull(toDate);
    }

    public static void validateIsFirstDateGreater(Date fromDate, Date toDate) {
        boolean fromDateGreaterThanToDate = isFirstDateGreater(fromDate, toDate);

        if (fromDateGreaterThanToDate) {
            log.error(INVALID_DATE_DEBUG_MESSAGE);
            throw new BadRequestException(INVALID_DATE_MESSAGE, INVALID_DATE_DEBUG_MESSAGE);
        }
    }

    public static void validateIfStartTimeGreater(Date startTime, Date endTime) {

        boolean isBothTimeEqual = startTime.equals(endTime);

        if (isBothTimeEqual) {
            log.error(EQUAL_DATE_TIME_MESSAGE);
            throw new BadRequestException(EQUAL_DATE_TIME_MESSAGE, EQUAL_DATE_TIME_DEBUG_MESSAGE);
        }

        boolean isStartTimeGreaterThanEndTime = startTime.after(endTime);

        if (isStartTimeGreaterThanEndTime) {
            log.error(INVALID_DATE_TIME_MESSAGE);
            throw new BadRequestException(INVALID_DATE_TIME_MESSAGE, INVALID_DATE_TIME_DEBUG_MESSAGE);
        }
    }
}
