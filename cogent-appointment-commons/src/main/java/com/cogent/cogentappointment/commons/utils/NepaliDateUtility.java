package com.cogent.cogentappointment.commons.utils;

import com.cogent.cogentappointment.commons.dto.request.date.DateConverterRequestDTO;
import com.cogent.cogentappointment.commons.dto.response.date.DateConverterResponeDTO;
import com.cogent.cogentappointment.commons.service.YearMonthDayService;
import com.cogent.cogentappointment.persistence.model.YearMonthDay;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

import static com.cogent.cogentappointment.commons.constants.DateConstants.MONTH_NAMES;

/**
 * @author Nikesh Maharjan(ERP)
 */
@Component
public class NepaliDateUtility {

    private final YearMonthDayService yearMonthDayService;

    public NepaliDateUtility(YearMonthDayService yearMonthDayService) {
        this.yearMonthDayService = yearMonthDayService;
    }

    /**
     * Method to convert english date into equivalent nepali date.
     *
     * @param englishDate array of string containing nepali date (first element) and nepali date string (second element)
     * @return
     */
    public String[]  getNepaliDateForDate(Date englishDate) {
        DateTime dt = new DateTime(englishDate);

        String[] result = {"", ""};
        DateConverterRequestDTO englishDateRequest = new DateConverterRequestDTO();
        englishDateRequest.setYear(dt.getYear());
        englishDateRequest.setMonth(dt.getMonthOfYear());
        englishDateRequest.setDay(dt.getDayOfMonth());

        DateConverterResponeDTO nepaliDateResponse = YearMonthDayUtils.convertFromADToBS(
                YearMonthDayUtils.getNepaliDateMap(yearMonthDayService.findAll()), englishDateRequest);
        //commented so as to remove extra 0 in front of single digit
        /*result[0] = nepaliDateResponse.getYear() + "-" +
                (nepaliDateResponse.getMonth() <= 9 ? "0" + nepaliDateResponse.getMonth() : nepaliDateResponse.getMonth()) + "-" +
                (nepaliDateResponse.getDay() <= 9 ? "0" + nepaliDateResponse.getDay() : nepaliDateResponse.getDay());*/
        result[0] = nepaliDateResponse.getYear() + "-" + nepaliDateResponse.getMonth() + "-" + nepaliDateResponse.getDay();
        result[1] = getReadableNepaliDate(nepaliDateResponse);
        return result;
    }
    public String getNepaliDateFromDate(Date englishDate) {
        DateTime dt = new DateTime(englishDate);

        DateConverterRequestDTO englishDateRequest = new DateConverterRequestDTO();
        englishDateRequest.setYear(dt.getYear());
        englishDateRequest.setMonth(dt.getMonthOfYear());
        englishDateRequest.setDay(dt.getDayOfMonth());

        DateConverterResponeDTO nepaliDateResponse = YearMonthDayUtils.convertFromADToBS(
                YearMonthDayUtils.getNepaliDateMap(yearMonthDayService.findAll()), englishDateRequest);
        //commented so as to remove extra 0 in front of single digit
        /*result[0] = nepaliDateResponse.getYear() + "-" +
                (nepaliDateResponse.getMonth() <= 9 ? "0" + nepaliDateResponse.getMonth() : nepaliDateResponse.getMonth()) + "-" +
                (nepaliDateResponse.getDay() <= 9 ? "0" + nepaliDateResponse.getDay() : nepaliDateResponse.getDay());*/
        return nepaliDateResponse.getYear() + "-" + nepaliDateResponse.getMonth() + "-" + nepaliDateResponse.getDay();
    }

    public static String getReadableNepaliDate(DateConverterResponeDTO nepaliDateResponse) {
        String result = MONTH_NAMES[nepaliDateResponse.getMonth()] + " ";

        result += (nepaliDateResponse.getDay() <= 9) ? "0" + nepaliDateResponse.getDay() : nepaliDateResponse.getDay();

        result += ", " + nepaliDateResponse.getYear();

        return result;
    }

    public static String getReadableNepaliDateFromNepaliDateString(String nepaliDateString) {
        String[] splits = nepaliDateString.split("-");

        int year = Integer.parseInt(splits[0]);
        int month = Integer.parseInt(splits[1]);
        int day = Integer.parseInt(splits[2]);

        String result = MONTH_NAMES[month] + " ";

        result += (day <= 9) ? "0" + day : day;

        result += ", " + year;

        return result;
    }

    //Input param in form '2074-05-25'
    public LocalDate getEnglishDateFromNepali(String nepaliDate) {
        DateConverterRequestDTO nepaliDateConverterRequestDTO = new DateConverterRequestDTO();
        nepaliDateConverterRequestDTO.setDay(Integer.parseInt(nepaliDate.split("-")[2]));
        nepaliDateConverterRequestDTO.setMonth(Integer.parseInt(nepaliDate.split("-")[1]));
        nepaliDateConverterRequestDTO.setYear(Integer.parseInt(nepaliDate.split("-")[0]));
        nepaliDateConverterRequestDTO.setConvertTo('B');

        DateConverterResponeDTO englishDateResponse = YearMonthDayUtils.convertFromBSToAD(
                YearMonthDayUtils.getNepaliDateMap(yearMonthDayService.findAll()), nepaliDateConverterRequestDTO);

        LocalDate englishDate = LocalDate.of(englishDateResponse.getYear(), englishDateResponse.getMonth(), englishDateResponse.getDay());

        return englishDate;
    }

    public Integer getLastDateOfAsarForGivenYear(Integer year) {
        YearMonthDay yearMonthDay = yearMonthDayService.findByYear(year);
        return yearMonthDay.getAshad();
    }

    public Integer getCurrentNepaliYear(){
        String[] nepaliDate = (getNepaliDateForDate(new Date()))[0].split("-");
        return Integer.parseInt(nepaliDate[0]);
    }
}
