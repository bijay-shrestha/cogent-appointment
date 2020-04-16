package com.cogent.cogentappointment.admin.utils;


import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cogent.cogentappointment.admin.utils.commons.DateConverterUtils.getFiscalYear;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.convertDateToLocalDate;

/**
 * @author Sauravi Thapa २०/२/१०
 */
public class DashboardUtils {

    private static int getNumberOfDaysInMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    private static List<Integer> getDatesOfMonth(int year, int month) {
        Integer numberOfDays = getNumberOfDaysInMonth(year, month);
        return IntStream.rangeClosed(1, numberOfDays).boxed().collect(Collectors.toList());
    }

    public static RevenueStatisticsResponseDTO parseToGenerateRevenueResponseDTO(
            Double currentTransaction,
            Double growthPercent,
            Character filterType,
            AppointmentRevenueStatisticsResponseDTO appointmentStatistics) {

        return RevenueStatisticsResponseDTO.builder()
                .amount(currentTransaction)
                .growthPercent(growthPercent)
                .fiscalYear(getFiscalYear())
                .filterType(filterType)
                .appointmentStatistics(appointmentStatistics)
                .build();
    }

    public static RevenueTrendResponseDTO revenueStatisticsResponseDTO(List<Object[]> resultList, Character filter) {
        RevenueTrendResponseDTO revenueTrendResponseDTO = new RevenueTrendResponseDTO();
        revenueTrendResponseDTO.setData(getMapFromObject(resultList));
        revenueTrendResponseDTO.setFilterType(filter);

        return revenueTrendResponseDTO;
    }

    private static Map<String, String> getMapFromObject(List<Object[]> resultList) {
        final int WEEK_MONTH_YEAR_INDEX = 0;
        final int TOTAL_REVENUE = 1;
        Map<String, String> map = new LinkedHashMap<>();
        resultList.stream().forEach(objects -> {
            map.put(objects[WEEK_MONTH_YEAR_INDEX].toString(), objects[TOTAL_REVENUE].toString());
        });

        return map;
    }

    private static final String ZERO = "0";
    private static final int NUMBER_OF_DAYS_OF_WEEK = 7;
    private static final int NUMBER_OF_MONTHS_OF_YEAR = 12;

    public static boolean isMapContainsEveryField(Map<String, String> map, Date currentDate, Character filter) {
        switch (filter) {
            case 'M':
                LocalDate toLocalDate = convertDateToLocalDate(currentDate);
                int year = toLocalDate.getMonthValue();
                int month = toLocalDate.getMonthValue();
                return map.size() == getDatesOfMonth(year, month)
                        .size() ? true : false;
            case 'W':
                return map.size() == NUMBER_OF_DAYS_OF_WEEK ? true : false;
            case 'Y':
                return map.size() == NUMBER_OF_MONTHS_OF_YEAR ? true : false;
        }
        return true;
    }


    public static Map<String, String> addRemainingFields(Map<String, String> map, Date fromDate,
                                                         Date toDate, Character filter) {

        LocalDate currentLocalDate = convertToLocalDateViaInstant(toDate);
        LocalDate previousLocalDate = convertToLocalDateViaInstant(fromDate);

        switch (filter) {
            case 'W':
                List<String> daysOfWeeks = getDaysOfWeekBetweenLocalDates(previousLocalDate, currentLocalDate);
                Map<String, String> daysOfWeekMap = new LinkedHashMap<>();
                daysOfWeeks.forEach(days -> {
                    if (map.containsKey(days)) {
                        //days contains daysOfWeeks, monthName and Year eg: SAT, Feb 10
                        daysOfWeekMap.put(days, map.get(days));
                    } else {
                        daysOfWeekMap.put(days, ZERO);
                    }
                });
                return daysOfWeekMap;

            case 'M':
                List<String> datesOfMonth = getDateBetweenLocalDates(previousLocalDate, currentLocalDate);
                Map<String, String> datesMap = new LinkedHashMap<>();
                datesOfMonth.forEach(month -> {
                    if (map.containsKey(month)) {
                        //dates contains dayOfMonth And monthName eg: 3 Feb
                        datesMap.put(month, map.get(month));
                    } else {
                        datesMap.put(month, ZERO);
                    }
                });
                return datesMap;

            case 'Y':
                List<String> monthsOfYear = getMonthsBetweenLocalDates(previousLocalDate, currentLocalDate);
                Map<String, String> months = new LinkedHashMap<>();
                monthsOfYear.forEach(month -> {
                    if (map.containsKey(month)) {
                        //month contains MonthName and year eg: Feburary,2020
                        months.put(month, map.get(month));
                    } else {
                        months.put(month, ZERO);
                    }
                });
                return months;
        }
        return map;
    }

    private static List<String> getDateBetweenLocalDates(LocalDate previous, LocalDate current) {
        List<String> dateBetweenLocalDates = new ArrayList<>();
        for (LocalDate localDate = previous; localDate.isBefore(current) ||
                localDate.isEqual(current); localDate = localDate.plusDays(1)) {
            dateBetweenLocalDates.add(localDate.getDayOfMonth() +
                    " " +
                    trimName(toTitleCase(localDate.getMonth().name().toLowerCase())));
        }
        return dateBetweenLocalDates;
    }

    private static List<String> getDaysOfWeekBetweenLocalDates(LocalDate previous, LocalDate current) {
        List<String> daysOfWeek = new ArrayList<>();
        final Integer ONE = 1;
        for (LocalDate localDate = previous; localDate.isBefore(current) ||
                localDate.isEqual(current); localDate = localDate.plusDays(ONE)) {
            daysOfWeek.add(trimName(localDate.getDayOfWeek().name()) +
                    "," +
                    trimName(toTitleCase(localDate.getMonth().name().toLowerCase())) +
                    " " +
                    localDate.getDayOfMonth());
        }
        return daysOfWeek;
    }

    private static List<String> getMonthsBetweenLocalDates(LocalDate previous, LocalDate current) {
        List<String> monthsOfYear = new ArrayList<>();
        final Integer ONE = 1;
        for (LocalDate localDate = previous;
             localDate.isBefore(current) || localDate.isEqual(current); localDate = localDate.plusMonths(ONE)) {
            monthsOfYear.add(trimName(toTitleCase(localDate.getMonth().name().toLowerCase())) +
                    "," +
                    localDate.getYear());
        }
        return monthsOfYear;
    }

    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    private static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static String trimName(String name) {
        return name.substring(0, 3);
    }

}
