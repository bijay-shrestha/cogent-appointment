package com.cogent.cogentappointment.admin.utils.commons;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;

/**
 * @author smriti ON 16/01/2020
 */
public class AgeConverterUtils {

    public static String calculateAge(Date date) {
//        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-30");

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

    public static String ageConverter(LocalDate date) {
        LocalDate birthday = date;
        LocalDate today = LocalDate.now();                          //Today's date
        Period p = Period.between(birthday, today);
        if ((p.getYears() == 0) && (p.getMonths() == 0)) {
            return (p.getDays() + "days");
        } else if (p.getYears() == 0) {
            return (p.getMonths() + "months");
        } else {
            return (p.getYears() + "years");
        }
    }
}
