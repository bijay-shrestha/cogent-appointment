package com.cogent.cogentappointment.client.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convert24HourTo12HourFormat;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferUtils {
    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static List<Date> mergeOverrideAndActualDateList(
            List<Date> overrideList,
            List<Date> actualList) {

        List<Date> unmatchedList = actualList.stream()
                .filter(actual -> (overrideList.stream()
                        .filter(override -> (override.equals(actual))
                                && (override.equals(actual))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        overrideList.addAll(unmatchedList);

//        overrideList.removeIf(override -> override.getDayOffStatus().equals(YES));

        return overrideList;
    }

    public static List<Date> getActualdate(List<String> dayOffDay, List<Date> dates) {
        List<Date> unmatched = dates.stream()
                .filter(actualDate -> dayOffDay.stream()
                        .filter(weekDay -> weekDay.equals(actualDate.toString().substring(0, 3).toUpperCase()))
                        .count() < 1)
                .collect(Collectors.toList());
        return unmatched;
    }

    public static List<String> getGapDuration(String startTime, String endTime, Integer gapDuration) {
        final Duration duration = Minutes.minutes(gapDuration).toStandardDuration();
        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));
        List<String> response = new ArrayList<>();

        do {
            response.add(convert24HourTo12HourFormat(FORMAT.print(dateTime)));
            dateTime = dateTime.plus(duration);
        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return response;
    }

    public static List<String> getVacantTime(List<String> allTimeSlot,
                                             List<String> unavailableTimeSlot,
                                             Date requestedDate){

        Date time = new java.util.Date(System.currentTimeMillis());
        String dateFormat=new SimpleDateFormat("HH:mm:ss").format(time);
        LocalTime localTime=LocalTime.parse(dateFormat);

        List<String> unmatchedList = allTimeSlot.stream()
                .filter(actual -> (unavailableTimeSlot.stream()
                        .filter(override -> (override.equals(actual))
                                && (override.equals(actual))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        return unmatchedList;
    }


}
