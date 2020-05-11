package com.cogent.cogentappointment.admin.repository;

import org.junit.Test;

import java.time.LocalTime;

/**
 * @author smriti on 06/05/20
 */
public class DateTest {

    @Test
    public void isTimeBetween() {
        String startTime = "09:00";
        String endTime = "17:49";

        LocalTime start = LocalTime.parse(startTime);
        LocalTime stop = LocalTime.parse(endTime);

        LocalTime target = LocalTime.parse("17:49");

        Boolean isBetweenStartAndStopStrictlySpeaking =
                ((!target.isBefore(start) && target.isBefore(stop)));

        System.out.println(isBetweenStartAndStopStrictlySpeaking);
    }
}
