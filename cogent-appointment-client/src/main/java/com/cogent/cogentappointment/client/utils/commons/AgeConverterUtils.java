package com.cogent.cogentappointment.client.utils.commons;

import java.time.LocalDate;
import java.time.Period;

/**
 * @author Sauravi Thapa 11/1/19
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
