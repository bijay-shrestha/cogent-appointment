package com.cogent.cogentappointment.admin.utils.commons;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;

/**
 * @author smriti ON 16/01/2020
 */
public class DateConverterUtils {

    public static String ageConverter(LocalDate date) {
        LocalDate birthday = date;
        LocalDate today = LocalDate.now();                          //Today's date
        Period p = Period.between(birthday, today);
        if ((p.getYears() == 0) && (p.getMonths() == 0)) {
            return (p.getDays() + " days");
        } else if (p.getYears() == 0) {
            return (p.getMonths() + " months");
        } else {
            return (p.getYears() + " years");
        }
    }

    public static Character dateDifference(Date toDate, Date fromDate) {

        LocalDate toLocalDate = convertDateToLocalDate(toDate);
        LocalDate toFromDate = convertDateToLocalDate(fromDate);
        int days;
        int years;
        int months;

        Period period = Period.between(toFromDate, toLocalDate);

        days = period.getDays();

        years = period.getYears();

        months = period.getMonths();

        Character pillType;

        if (years==0 && months==0 && days <= 1) {
            pillType = 'D';
        } else if (years==0 && months==0 && days > 1 && days <= 7) {
            pillType = 'W';
        } else if (years==0 || months>=1 || days>7 ) {
            pillType = 'M';
        } else if(years>=1) {
            pillType = 'Y';
        }else {
            return null;
        }

        return pillType;
    }

    public static String getFiscalYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        if (month < 3) {
            return (year - 1) + "/" + year;
        } else {
            return year + "/" + (year + 1);
        }
    }


}
