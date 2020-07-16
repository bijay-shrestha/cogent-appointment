package com.cogent.cogentappointment.esewa.repository;

import org.junit.Test;

import static com.cogent.cogentappointment.esewa.constants.UtilityConfigConstants.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getMonthFromNepaliDate;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getYearFromNepaliDate;

/**
 * @author smriti ON 19/01/2020
 */
public class AgeTest {

    @Test
    public void testFiscalYear() {
        String nepaliCreatedDate = "2079-03-31";

        int year = getYearFromNepaliDate(nepaliCreatedDate);
        int month = getMonthFromNepaliDate(nepaliCreatedDate);

        String startingFiscalYear = start(year, month);
        String endingFiscalYear = end(year, month);

        System.out.println("START-------" + startingFiscalYear);
        System.out.println("END----------" + endingFiscalYear);
    }

    private String start(int year, int month) {

        if (month == APPLICATION_STARTING_FISCAL_MONTH)
            return year + APPLICATION_STARTING_FISCAL_DAY;

        boolean check = month < APPLICATION_STARTING_FISCAL_MONTH;
        if (check) {
            return year - 1 + APPLICATION_STARTING_FISCAL_DAY;
        } else {
            return year + APPLICATION_STARTING_FISCAL_DAY;
        }
    }

    private String end(int year, int month) {

        if (month == APPLICATION_STARTING_FISCAL_MONTH)
            return year + 1 + APPLICATION_ENDING_FISCAL_DAY;

        boolean check = month < APPLICATION_STARTING_FISCAL_MONTH;

        if (check) {
            return year + APPLICATION_ENDING_FISCAL_DAY;
        } else {
            return year + 1 + APPLICATION_ENDING_FISCAL_DAY;
        }
    }
}
