package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorWithSpecialization;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferUtils {
    public static List<Date> mergeOverrideAndActualDoctorList(
            List<Date> overrideList,
            List<Date> actualList) {

        List<Date> unmatchedList = actualList.stream()
                .filter(actual -> (overrideList.stream()
                        .filter(override -> (override.equals(actual))
                                && (override.equals(actual))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        overrideList.addAll(unmatchedList);

//        overrideList.removeIf(override -> override.getDayOffStatus().equals(YES));

        return overrideList;
    }

    public static List<Date> getActualdate( List<String> dayOffDay,List<Date>  dates){
        List<Date> unmatched = dates.stream()
                .filter(test -> dayOffDay.stream()
                        .filter(weekDay -> weekDay.equals(test.toString().substring(0, 3).toUpperCase()))
                        .count() < 1)
                .collect(Collectors.toList());
        return unmatched;
    }


}
