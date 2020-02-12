package com.cogent.cogentappointment.client.utils.commons;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertDateToLocalDate;

/**
 * @author smriti ON 16/01/2020
 */
public class DateConverterUtils {

    public static String calculateAge(Date date) {

        LocalDate today = LocalDate.now();
        LocalDate birthDate = convertDateToLocalDate(date);

        Period period = Period.between(birthDate, today);

        int years = period.getYears();
        int month = period.getMonths();
        int days = period.getDays();

        String age;

        if (years <= 0 && month <= 0) {
            age = days + " days";
        } else if (years <= 0) {
            age = month + " months";
        } else {
            age = years + " years";
        }

        return age;
    }

    public static Character dateDifference(Date toDate, Date fromDate) {

        LocalDate toLocalDate = convertDateToLocalDate(toDate);
        LocalDate toFromDate = convertDateToLocalDate(fromDate);
        int days;
        int years;

        Period period = Period.between(toFromDate, toLocalDate);

        days = period.getDays();
        years = period.getYears();

        Character pillType;

        if (years == 0 && days <= 1) {
            pillType = 'D';
        } else if (years == 0 && days > 1 && days <= 7) {
            pillType = 'W';
        } else if (years == 0 && days > 7 && days <= 31) {
            pillType = 'M';
        } else {
            pillType = 'Y';
        }

        return pillType;
    }
}
