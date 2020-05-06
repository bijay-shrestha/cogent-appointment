package com.cogent.cogentappointment.client;

import org.junit.Test;

import java.time.LocalTime;

/**
 * @author smriti on 06/05/20
 */
public class DateTest {

    @Test
    public void isTimeBetween() {
        String startTime = "20:11";
        String endTime = "14:49";

        LocalTime start = LocalTime.parse(startTime);
        LocalTime stop = LocalTime.parse("14:49");

        LocalTime target = LocalTime.parse("20:30");

        Boolean isBetweenStartAndStopStrictlySpeaking =
                ((!target.isBefore(start) && target.isBefore(stop)));

        System.out.println(isBetweenStartAndStopStrictlySpeaking);
    }
}
