package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cogent.cogentappointment.client.utils.commons.DateConverterUtils.getFiscalYear;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertDateToLocalDate;

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
            Character revenueType,
            AppointmentRevenueStatisticsResponseDTO appointmentStatistics) {

        return RevenueStatisticsResponseDTO.builder()
                .amount(currentTransaction)
                .growthPercent(growthPercent)
                .fiscalYear(getFiscalYear())
                .filterType(revenueType)
                .appointmentStatistics(appointmentStatistics)
                .build();
    }

    public static RevenueTrendResponseDTO revenueStatisticsResponseDTO(List<Object[]> resultList, Character filter) {
        RevenueTrendResponseDTO revenueTrendResponseDTO = new RevenueTrendResponseDTO();
        revenueTrendResponseDTO.setData(getMapFromObject(resultList));
        revenueTrendResponseDTO.setFilterType(filter);

        return revenueTrendResponseDTO;
    }

    public static OverallRegisteredPatientsResponseDTO parseToOverallRegisteredPatientsResponseDTO(Long registeredpatientCount,
                                                                                                   Character pillType) {
        OverallRegisteredPatientsResponseDTO overallRegisteredPatientsResponseDTO =
                new OverallRegisteredPatientsResponseDTO();
        overallRegisteredPatientsResponseDTO.setRegisteredPatient(registeredpatientCount);
        overallRegisteredPatientsResponseDTO.setPillType(pillType);

        return overallRegisteredPatientsResponseDTO;
    }


    private static Map<String, String> getMapFromObject(List<Object[]> resultList) {
        final int WEEK_MONTH_YEAR_INDEX = 0; //VARIES ACCORDING TO FILTER
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

    public static List<DoctorRevenueDTO> mergeDoctorAndCancelledRevenue(List<DoctorRevenueDTO> doctorRevenueResponse,
                                                                        List<DoctorRevenueDTO> cancelledRevenueResponse) {

        List<DoctorRevenueDTO> combinedDoctorRevenueResponse =
                combineDoctorAndCancelledRevenue(doctorRevenueResponse, cancelledRevenueResponse);

        List<DoctorRevenueDTO> finalRevenueResponse = new ArrayList<>();

        combinedDoctorRevenueResponse.forEach(combinedInfo -> {

                    DoctorRevenueDTO doctorRevenueDTO = finalRevenueResponse.stream()
                            .filter(finalRevenue ->
                                    isDoctorRevenueConditionMatched(combinedInfo, finalRevenue)
                            ).findAny().orElse(null);

                    //means finalRevenueResponse already contains details of same doctor & specialization
                    if (!Objects.isNull(doctorRevenueDTO)) {
                        calculateMatchedRevenueDetails(doctorRevenueDTO, combinedInfo);
                    } else {
                        //means this is new record of doctor & specialization
                        calculateUnmatchedMatchedRevenueDetails(combinedInfo);

                        finalRevenueResponse.add(combinedInfo);
                    }
                }
        );

        return finalRevenueResponse;
    }

    private static boolean isDoctorRevenueConditionMatched(DoctorRevenueDTO doctorRevenue,
                                                           DoctorRevenueDTO cancelledRevenue) {
        return doctorRevenue.getDoctorId().equals(cancelledRevenue.getDoctorId())
                && (doctorRevenue.getSpecializationId().equals(cancelledRevenue.getSpecializationId()));

    }

    private static List<DoctorRevenueDTO> combineDoctorAndCancelledRevenue(List<DoctorRevenueDTO> doctorRevenueResponse,
                                                                           List<DoctorRevenueDTO> cancelledRevenueResponse) {

        List<DoctorRevenueDTO> combinedDoctorRevenueResponse = new ArrayList<>();
        combinedDoctorRevenueResponse.addAll(doctorRevenueResponse);
        combinedDoctorRevenueResponse.addAll(cancelledRevenueResponse);

        return combinedDoctorRevenueResponse;
    }

    private static void calculateMatchedRevenueDetails(DoctorRevenueDTO doctorRevenueDTO,
                                                       DoctorRevenueDTO combinedInfo) {

        doctorRevenueDTO.setSuccessfulAppointments(
                combinedInfo.getSuccessfulAppointments() + doctorRevenueDTO.getSuccessfulAppointments());

        doctorRevenueDTO.setCancelledAppointments(
                combinedInfo.getCancelledAppointments() + doctorRevenueDTO.getCancelledAppointments());

        doctorRevenueDTO.setTotalAppointments(doctorRevenueDTO.getSuccessfulAppointments() +
                doctorRevenueDTO.getCancelledAppointments());

        doctorRevenueDTO.setDoctorRevenue(
                combinedInfo.getDoctorRevenue() + doctorRevenueDTO.getDoctorRevenue());

        doctorRevenueDTO.setCancelledRevenue(
                combinedInfo.getCancelledRevenue() + doctorRevenueDTO.getCancelledRevenue());

        doctorRevenueDTO.setTotalRevenue(
                doctorRevenueDTO.getDoctorRevenue() + doctorRevenueDTO.getCancelledRevenue());
    }

    private static void calculateUnmatchedMatchedRevenueDetails(DoctorRevenueDTO combinedInfo) {

        combinedInfo.setTotalAppointments(combinedInfo.getSuccessfulAppointments() +
                combinedInfo.getCancelledAppointments());

        combinedInfo.setTotalRevenue(combinedInfo.getDoctorRevenue() + combinedInfo.getCancelledRevenue());
    }

    public static DoctorRevenueResponseDTO parseToDoctorRevenueResponseDTO(List<DoctorRevenueDTO> revenueDTOList) {

        Long overallAppointmentCount = revenueDTOList.stream()
                .mapToLong(DoctorRevenueDTO::getTotalAppointments)
                .sum();

        Double totalRevenueAmount = revenueDTOList.stream()
                .mapToDouble(DoctorRevenueDTO::getTotalRevenue)
                .sum();

        revenueDTOList.sort((o1, o2) -> Double.compare(o2.getTotalRevenue(), o1.getTotalRevenue()));

        return DoctorRevenueResponseDTO.builder()
                .doctorRevenueInfo(revenueDTOList)
                .totalAppointmentCount(overallAppointmentCount)
                .totalRevenueAmount(totalRevenueAmount)
                .build();
    }
}
