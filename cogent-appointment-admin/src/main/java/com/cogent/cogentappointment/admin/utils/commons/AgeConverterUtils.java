package com.cogent.cogentappointment.admin.utils.commons;

import java.time.LocalDate;
import java.time.Period;

/**
 * @author smriti ON 16/01/2020
 */
public class AgeConverterUtils {

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
