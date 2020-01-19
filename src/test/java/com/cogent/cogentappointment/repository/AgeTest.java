package com.cogent.cogentappointment.repository;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * @author smriti ON 19/01/2020
 */
public class AgeTest {

    @Test
    public void calculateAgeFromDate() {
        LocalDate today = LocalDate.now();                          //Today's date
        LocalDate birthday = LocalDate.of(1995, Month.OCTOBER, 1);  //Birth date

        Period period = Period.between(birthday, today);

//Now access the values as below
        System.out.println(period.getDays());
        System.out.println(period.getMonths());
        System.out.println(period.getYears());
        System.out.println("Your age is:" + period.getYears() + "years " + period.getMonths() + "months " +
                period.getDays() + "days ");
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
    public void calculateDOB(){
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
