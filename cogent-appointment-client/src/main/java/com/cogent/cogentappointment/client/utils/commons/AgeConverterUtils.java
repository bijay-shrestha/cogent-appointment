package com.cogent.cogentappointment.client.utils.commons;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertDateToLocalDate;

/**
 * @author Sauravi Thapa 11/1/19
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
}
