package com.cogent.cogentappointment.admin.repository;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;


/**
 * @author smriti ON 19/01/2020
 */
public class AgeTest {

    @Test
    public void calculateAgeFromDate() throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-30");

        LocalDate today = LocalDate.now();                          //Today's date
        LocalDate birthday = convertDateToLocalDate(date);  //Birth date

        Period period = Period.between(birthday, today);

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

        System.out.println(age);
    }

    @Test
    public void ageConverter() {
        LocalDate birthday = LocalDate.of(2019, Month.DECEMBER, 1);

        LocalDate today = LocalDate.now();                          //Today's date
        Period p = Period.between(birthday, today);
        if ((p.getYears() == 0) && (p.getMonths() == 0)) {
            System.out.println(p.getDays() + "days");
        } else if (p.getYears() == 0) {
            System.out.println(p.getMonths() + "months");
        } else {
            System.out.println(p.getYears() + "years");
        }
    }

    @Test
    public void calculateDOB() {
        int dobYear = 24;               //age in yrs
        int dobMonth = 3;               //age in months
        int dobDay = 18;                //age in days

        LocalDate now = LocalDate.now();
        LocalDate dob = now.minusYears(dobYear)
                .minusMonths(dobMonth)
                .minusDays(dobDay);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(dob.format(formatter));
    }
}
